package dev.nubankchallenge

import _root_.io.circe.generic.auto._
import _root_.io.circe.syntax._
import cats.data.NonEmptyList
import cats.effect._
import cats.implicits._
import dev.nubankchallenge.domain.Tax
import dev.nubankchallenge.json.InputParser
import dev.nubankchallenge.logic.TaxCalculator
import fs2._
import fs2.io._
import org.typelevel.log4cats._
import org.typelevel.log4cats.slf4j._

object Solution extends IOApp with InputParser with TaxCalculator {

  implicit val logging: LoggerFactory[IO] = Slf4jFactory.create[IO]

  val logger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  private def calculateTax(rawSession: String): NonEmptyList[Tax] = {
    val session = this.parse(rawSession)
    val tax     = this.calculate(session)

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
