package dev.nubankchallenge.logic.impl

import scala.annotation.tailrec

import dev.nubankchallenge.domain._
import dev.nubankchallenge.logic.TaxCalculator

class StockTradeTaxCalculator(taxExemptionLimit: BigDecimal, taxRate: BigDecimal) extends TaxCalculator {

  override def calculate(session: List[Transaction]): List[Tax] = {
    @tailrec
    def loop(session: List[Transaction], state: SessionState, accumulator: List[Tax]): List[Tax] = session match {
      case head :: tail =>
        val (newState, tax) = step(head, state)
        loop(tail, newState, accumulator :+ tax)
      case Nil => accumulator
    }

    val initialState = SessionState()
    val taxes        = List.empty[Tax]
    loop(session, initialState, taxes)
  }

  private def step(in: Transaction, state: SessionState): (SessionState, Tax) = {
    (in.op, in.unitCost.price < state.weightedAveragePrice) match {
      case (Buy, _) =>
        val newState = SessionState(
          accumulatedProfit = state.accumulatedProfit,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value + in.quantity.value),
          weightedAveragePrice = SessionState.recalculateWeightedAverage(in, state)
        )
        val tax = Tax.zero
        (newState, tax)
      case (Sell, true) =>
        // sell with loss
        val lossAmount = in.unitCost.price * in.quantity.value
        val newState = SessionState(
          accumulatedProfit = state.accumulatedProfit - lossAmount,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value),
          weightedAveragePrice = state.weightedAveragePrice
        )
        val tax = Tax.zero
        (newState, tax)
      case (Sell, false) =>
        // sell with gain
        val gainAmount = in.unitCost.price * in.quantity.value
        val newState = SessionState(
          accumulatedProfit = state.accumulatedProfit + gainAmount,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value),
          weightedAveragePrice = state.weightedAveragePrice
        )
        val tax =
          if (newState.accumulatedProfit > taxExemptionLimit) { Tax(taxRate * newState.accumulatedProfit) }
          else { Tax.zero }
        (newState, tax)
    }
  }
}
