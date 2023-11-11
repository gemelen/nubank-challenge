package dev.nubankchallenge.domain

import io.circe.Decoder

sealed trait Operation
case object Buy  extends Operation
case object Sell extends Operation

object Operation {
  given operationDecoder: Decoder[Operation] = Decoder[String].emap {
    case "buy"        => Right(Buy)
    case "sell"       => Right(Sell)
    case anythingElse => Left(s"Unexpected value for an operation: $anythingElse")
  }
}
