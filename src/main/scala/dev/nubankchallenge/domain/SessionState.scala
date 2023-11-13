package dev.nubankchallenge.domain

case class SessionState(
    accumulatedProfit: BigDecimal = BigDecimal.valueOf(0L),
    accumulatedStockQuantity: Quantity = Quantity(),
    weightedAveragePrice: BigDecimal = BigDecimal(0L)
)

object SessionState {

  // new-weighted-average-price =
  //    ( (current-stock-quantity * weighted-average-price) + (new-stock-quantity * new-price))
  //    /
  //    ( current-stock-quantity + new-stock-quantity)
  def recalculateWeightedAverage(in: Transaction, state: SessionState): BigDecimal = {
    val currentStock  = state.accumulatedStockQuantity.value
    val newStock      = in.quantity.value
    val newTotalStock = state.accumulatedStockQuantity.value + in.quantity.value

    ((state.weightedAveragePrice * currentStock + in.unitCost.price * newStock) / newTotalStock).setScale(2)
  }
}
