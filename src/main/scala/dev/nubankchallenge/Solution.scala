package dev.nubankchallenge

import _root_.io.circe.syntax._
import cats.effect._
import cats.implicits._
import dev.nubankchallenge.domain.Tax
import dev.nubankchallenge.json.InputParser
import dev.nubankchallenge.logic.TaxCalculator
import dev.nubankchallenge.logic.impl.StockTradeTaxCalculator
import fs2._
import fs2.io._

object Solution extends IOApp with InputParser {

  lazy val taxCalculator: TaxCalculator =
    StockTradeTaxCalculator(taxExemptionLimit = BigDecimal.valueOf(20_000L), taxRate = BigDecimal.valueOf(0.2))

  private def calculateTax(rawSession: String): List[Tax] = {
    val session = this.parse(rawSession)
    val tax     = taxCalculator.calculate(session)

    tax
  }

  override def run(args: List[String]): IO[ExitCode] = {
    val input =
      stdinUtf8[IO](1024)
        .repartition(s => Chunk.array(s.split("\n", -1)))
        .filter(_.nonEmpty)

    input
      .map(calculateTax)
      .foreach(s => IO.println(s.asJson.noSpaces))
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
