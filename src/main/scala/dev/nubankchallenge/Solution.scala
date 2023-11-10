package dev.nubankchallenge

import cats.effect._
import cats.effect.std.Console
import cats.implicits._
import fs2._
import fs2.io._
import org.typelevel.log4cats._
import org.typelevel.log4cats.slf4j._

object Solution extends IOApp {
  implicit val logging: LoggerFactory[IO]   = Slf4jFactory[IO]
  val logger: SelfAwareStructuredLogger[IO] = LoggerFactory[IO].getLogger

  override def run(args: List[String]): IO[ExitCode] = {
    val input = stdinUtf8[IO](1024).repartition(s => Chunk.array(s.split("\n", -1))).filter(_.nonEmpty)
    input.foreach(IO.println).compile.drain.as(ExitCode.Success)
  }
}
