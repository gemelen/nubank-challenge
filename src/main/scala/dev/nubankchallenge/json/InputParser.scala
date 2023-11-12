package dev.nubankchallenge.json

import dev.nubankchallenge.domain.Transaction
import io.circe.Error
import io.circe.parser.decode

trait InputParser {

  def parse(line: String): List[Transaction] = {
    // This is perfectly fine and production-ready,
    val maybeTransactions: Either[Error, List[Transaction]] = decode[List[Transaction]](line)
    // and this is not, but we are explicitly told to trust our input.
    maybeTransactions.getOrElse(List.empty[Transaction])
  }
}
