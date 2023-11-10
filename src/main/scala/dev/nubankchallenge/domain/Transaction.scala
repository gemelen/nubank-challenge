package dev.nubankchallenge.domain

case class Transaction(op: Operation, unitCost: UnitCost, quantity: Quantity)
