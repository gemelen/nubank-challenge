package dev.nubankchallenge.json

import dev.nubankchallenge.domain._
import munit.FunSuite

class InputSuite extends FunSuite {

  val test_case_1 = new Fixture[String]("test case files") {

    override def apply(): String = {
      val cl = getClass().getClassLoader()
      new String(cl.getResourceAsStream("fixtures/test_case_1.txt").readAllBytes())
    }

  }

  test("input is parsed correctly") {
    val expected =
      List(
        Transaction(op = Buy, unitCost = UnitCost(price = 10.00), quantity = Quantity(value = 100)),
        Transaction(op = Sell, unitCost = UnitCost(price = 15.00), quantity = Quantity(value = 50)),
        Transaction(op = Sell, unitCost = UnitCost(price = 15.00), quantity = Quantity(value = 50))
      )

    assertEquals(expected, new Object with InputParser().parse(test_case_1()))
  }
}
