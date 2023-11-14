package dev.nubankchallenge.domain

import munit.FunSuite

class DomainObjectsSuite extends FunSuite {

  test("weighted average price calculated correctly") {
    /*
      buy 10 at price 20 => totalGain = 0, totalQuantity = 10, price = 20
      sell 5 at 20 => totalGain = 100, totalQuantity = 5, price 20
      buy 5 at price 10 => totalGain = 100, totalQuantity = 5, price = ((5 x 20.00) + (5 x 10.00)) / (5 + 5) = 15.00
     */

    val in = Transaction(Buy, UnitCost(BigDecimal(10L)), Quantity(5L))
    val state = SessionState(
      profit = BigDecimal(100L),
      accumulatedStockQuantity = Quantity(5L),
      weightedAveragePrice = BigDecimal(20L)
    )
    assertEquals(SessionState.recalculateWeightedAverage(in, state), BigDecimal(15L))

  }

}
