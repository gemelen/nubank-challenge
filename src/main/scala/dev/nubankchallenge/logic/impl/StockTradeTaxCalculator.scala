package dev.nubankchallenge.logic.impl

import scala.annotation.tailrec

import dev.nubankchallenge.domain._
import dev.nubankchallenge.logic.TaxCalculator

class StockTradeTaxCalculator(taxExemptionLimit: BigDecimal, taxRate: BigDecimal) extends TaxCalculator {

  sealed trait TransactionResult
  case object Gain           extends TransactionResult
  case object Loss           extends TransactionResult
  case object NorGainNorLoss extends TransactionResult

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
    val resultDiscriminator = in.unitCost.price - state.weightedAveragePrice
    val transactionResult = resultDiscriminator match {
      case rd if rd < 0 => Loss
      case rd if rd > 0 => Gain
      case _            => NorGainNorLoss
    }

    (in.op, transactionResult) match {
      case (Buy, _) =>
        val newState = SessionState(
          accumulatedProfit = state.accumulatedProfit,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value + in.quantity.value),
          weightedAveragePrice = SessionState.recalculateWeightedAverage(in, state)
        )
        val tax = Tax.zero
        println(s"buy, state: ${newState}, tax: ${tax}")
        (newState, tax)
      case (Sell, Gain) =>
        val gainAmount = in.unitCost.price * in.quantity.value
        val newState = SessionState(
          accumulatedProfit = state.accumulatedProfit + gainAmount,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value),
          weightedAveragePrice = SessionState.recalculateWeightedAverage(in, state)
        )
        val tax =
          if (gainAmount > taxExemptionLimit) { Tax(taxRate * newState.accumulatedProfit) }
          else { Tax.zero }
        println(s"sell with gain, state: ${newState}, tax: ${tax}")
        (newState, tax)
      case (Sell, Loss) =>
        val lossAmount = in.unitCost.price * in.quantity.value
        val newState = SessionState(
          accumulatedProfit = state.accumulatedProfit - lossAmount,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value),
          weightedAveragePrice = SessionState.recalculateWeightedAverage(in, state)
        )
        val tax = Tax.zero
        println(s"sell with loss, state: ${newState}, tax: ${tax}")
        (newState, tax)
      case (Sell, NorGainNorLoss) =>
        val newState = SessionState(
          accumulatedProfit = state.accumulatedProfit,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value),
          weightedAveragePrice = SessionState.recalculateWeightedAverage(in, state)
        )
        val tax = Tax.zero
        println(s"sell without gain or loss, state: ${newState}, tax: ${tax}")
        (newState, tax)
    }
  }
}
