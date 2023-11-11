package dev.nubankchallenge.domain

import cats.Show
import io.circe.generic.auto._
import io.circe.syntax._

case class Tax(tax: BigDecimal)

object Tax {
  given showTax: Show[Tax] = Show.show(_.asJson.noSpaces.toString)
}
