package dev.nubankchallenge.domain

import java.math.MathContext
import java.math.RoundingMode

import cats.Show

case class SessionState(
    profit: BigDecimal = BigDecimal.valueOf(0L),
    revenue: BigDecimal = BigDecimal.valueOf(0L),
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

  given showSessionState: Show[SessionState] = Show.show { state =>
    s"Profit = ${state.profit}, revenue = ${state.revenue}, quantity = ${state.accumulatedStockQuantity.value}, price = ${state.weightedAveragePrice.toLong}"
  }
}
