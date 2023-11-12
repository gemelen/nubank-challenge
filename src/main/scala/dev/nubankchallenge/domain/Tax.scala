package dev.nubankchallenge.domain

import cats.Show
import io.circe._
import io.circe.syntax._

case class Tax(tax: BigDecimal)

object Tax {
  def zero: Tax            = Tax(BigDecimal.valueOf(0L))
  given showTax: Show[Tax] = Show.show(_.asJson.noSpaces.toString)
  given taxEncoder: Encoder[Tax] = new Encoder[Tax] {

    override def apply(a: Tax): Json = Json.obj(
      ("tax", Json.fromBigDecimal(a.tax.setScale(2)))
    )

  }
}
