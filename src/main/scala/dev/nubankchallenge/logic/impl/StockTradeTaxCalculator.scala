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
        val newState = state.copy(
          profit = if (state.accumulatedStockQuantity.value == 0) 0 else state.profit,
          revenue = if (state.accumulatedStockQuantity.value == 0) 0 else state.revenue,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value + in.quantity.value),
          weightedAveragePrice = SessionState.recalculateWeightedAverage(in, state)
        )
        val tax = Tax.zero
        (newState, tax)
      case (Sell, Gain) =>
        val gainAmount = (in.unitCost.price - state.weightedAveragePrice) * in.quantity.value
        val tax =
          if ((state.revenue + gainAmount) > taxExemptionLimit && (state.profit + gainAmount) > 0) {
            Tax(taxRate * (state.profit + gainAmount))
          } else { Tax.zero }
        val newState = state.copy(
          profit = if (tax.tax > 0) 0 else state.profit + gainAmount,
          revenue = if (tax.tax > 0) 0 else state.revenue + gainAmount,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value)
        )
        (newState, tax)
      case (Sell, Loss) =>
        val lossAmount = (state.weightedAveragePrice - in.unitCost.price) * in.quantity.value
        val newState = state.copy(
          profit = state.profit - lossAmount,
          revenue = state.revenue + lossAmount,
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value)
        )
        val tax = Tax.zero
        (newState, tax)
      case (Sell, NorGainNorLoss) =>
        val newState = state.copy(
          accumulatedStockQuantity = Quantity(state.accumulatedStockQuantity.value - in.quantity.value)
        )
        val tax = Tax.zero
        (newState, tax)
    }
  }
}
