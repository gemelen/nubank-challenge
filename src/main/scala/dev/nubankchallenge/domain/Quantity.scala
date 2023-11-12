package dev.nubankchallenge.domain

case class Quantity(value: BigDecimal = BigDecimal(0L))
object Quantity {
  def apply(value: BigInt): Quantity = Quantity(BigDecimal(value))
}
