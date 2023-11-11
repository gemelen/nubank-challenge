package dev.nubankchallenge.domain

import io.circe._

case class Transaction(op: Operation, unitCost: UnitCost, quantity: Quantity)
object Transaction {
  given transactionDecoder: Decoder[Transaction] =
    new Decoder[Transaction] {
      final def apply(hc: HCursor): Decoder.Result[Transaction] =
        for {
          operation <- hc.downField("operation").as[Operation]
          unitCost  <- hc.downField("unit-cost").as[BigDecimal]
          quantity  <- hc.downField("quantity").as[BigInt]
        } yield (Transaction(operation, UnitCost(unitCost), Quantity(quantity)))
    }
}
