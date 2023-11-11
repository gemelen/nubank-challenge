package dev.nubankchallenge.logic

import cats.data.NonEmptyList
import dev.nubankchallenge.domain.Tax
import dev.nubankchallenge.domain.Transaction

trait TaxCalculator {
  def calculate(session: NonEmptyList[Transaction]): Tax = ???
}
