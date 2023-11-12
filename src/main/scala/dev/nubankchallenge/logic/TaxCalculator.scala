package dev.nubankchallenge.logic

import dev.nubankchallenge.domain._

trait TaxCalculator {

  def calculate(session: List[Transaction]): List[Tax]
}
