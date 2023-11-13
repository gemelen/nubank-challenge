package dev.nubankchallenge.logic

import dev.nubankchallenge.domain._
import dev.nubankchallenge.json.InputParser
import dev.nubankchallenge.logic.impl.StockTradeTaxCalculator
import munit.FunSuite

class CalculationSuite extends FunSuite {

  lazy val parser = new Object with InputParser
  lazy val calculator =
    StockTradeTaxCalculator(taxExemptionLimit = BigDecimal.valueOf(20_000L), taxRate = BigDecimal.valueOf(0.2))

  val test_case_1 = new Fixture[List[Transaction]]("test case 1") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_1.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_2 = new Fixture[List[Transaction]]("test case 2") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_2.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_1_plus_2 = new Fixture[List[Transaction]]("test case 1 plus 2") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_1_plus_2.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_3 = new Fixture[List[Transaction]]("test case 3") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_3.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_4 = new Fixture[List[Transaction]]("test case 4") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_4.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_5 = new Fixture[List[Transaction]]("test case 5") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_5.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_6 = new Fixture[List[Transaction]]("test case 6") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_6.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_7 = new Fixture[List[Transaction]]("test case 7") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_7.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_8 = new Fixture[List[Transaction]]("test case 8") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_8.txt").readAllBytes())
      parser.parse(raw)
    }
  }
  val test_case_9 = new Fixture[List[Transaction]]("test case 9") {
    override def apply(): List[Transaction] = {
      val cl  = getClass().getClassLoader()
      val raw = new String(cl.getResourceAsStream("fixtures/test_case_9.txt").readAllBytes())
      parser.parse(raw)
    }
  }

  test("case #1") {
    val expected = List(Tax(0), Tax(0), Tax(0))
    assertEquals(expected, calculator.calculate(test_case_1()))
  }

  test("case #2") {
    val expected = List(Tax(0), Tax(10000), Tax(0))
    assertEquals(expected, calculator.calculate(test_case_2()))
  }
  test("case #1 + case #2") {
    val expected = List.empty // FIXME
    assertEquals(expected, calculator.calculate(test_case_1_plus_2()))
  }
  test("case #3") {
    val expected = List(Tax(0), Tax(0), Tax(1000))
    assertEquals(expected, calculator.calculate(test_case_3()))
  }
  test("case #4") {
    val expected = List(Tax(0), Tax(0), Tax(0))
    assertEquals(expected, calculator.calculate(test_case_4()))
  }
  test("case #5") {
    val expected = List(Tax(0), Tax(0), Tax(0), Tax(10000))
    assertEquals(expected, calculator.calculate(test_case_5()))
  }
  test("case #6") {
    val expected = List(Tax(0), Tax(0), Tax(0), Tax(0), Tax(3000))
    assertEquals(expected, calculator.calculate(test_case_6()))
  }
  test("case #7") {
    val expected = List(Tax(0), Tax(0), Tax(0), Tax(0), Tax(3000), Tax(0), Tax(0), Tax(3700), Tax(0))
    assertEquals(expected, calculator.calculate(test_case_7()))
  }
  test("case #8") {
    val expected = List(Tax(0), Tax(80000), Tax(0), Tax(60000))
    assertEquals(expected, calculator.calculate(test_case_8()))
  }
  test("case #9") {
    val expected = List(Tax(0), Tax(0), Tax(0), Tax(0), Tax(0), Tax(0), Tax(1000), Tax(2400))
    assertEquals(expected, calculator.calculate(test_case_9()))
  }
}
