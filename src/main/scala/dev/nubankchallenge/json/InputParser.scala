package dev.nubankchallenge.json

import cats.data.NonEmptyList
import dev.nubankchallenge.domain.Transaction
import io.circe.Error
import io.circe.parser.decode

trait InputParser {

  def parse(line: String): NonEmptyList[Transaction] = {
    // this is perfectly fine and production-ready
    val maybeTransactions: Either[Error, List[Transaction]] = decode[List[Transaction]](line)
    // and this is not, but we are explicitly told to trust our input
    // here we do a "leap of faith" twice:
    // 1. trusting the argument of `fromListUnsafe` is not empty list
    // 2. breaking out of Either and putting empty list for the case if we didn't parse our input
    // Unless our input insn't empty and matches the schema exactly, our application would crash
    NonEmptyList.fromListUnsafe(maybeTransactions.getOrElse(List.empty))
  }
}
