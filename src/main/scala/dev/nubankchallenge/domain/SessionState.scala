package dev.nubankchallenge.domain

import java.math.MathContext
import java.math.RoundingMode

case class SessionState(
    accumulatedProfit: BigDecimal = BigDecimal.valueOf(0L),
    accumulatedStockQuantity: Quantity = Quantity(),
    weightedAveragePrice: BigDecimal = BigDecimal(0L)
)

object SessionState {

  lazy val mc = new MathContext(2, RoundingMode.HALF_EVEN)

  // new-weighted-average-price =
  //    ( (current-stock-quantity * weighted-average-price) + (new-stock-quantity * new-price))
  //    /
  //    ( current-stock-quantity + new-stock-quantity)
  def recalculateWeightedAverage(in: Transaction, state: SessionState): BigDecimal = {
    val currentStock  = state.accumulatedStockQuantity.value
    val newStock      = in.quantity.value
    val newTotalStock = state.accumulatedStockQuantity.value + in.quantity.value

    ((state.weightedAveragePrice * currentStock + in.unitCost.price * newStock) / newTotalStock).round(mc)
  }
}
