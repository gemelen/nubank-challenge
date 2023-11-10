package dev.nubankchallenge.domain

sealed trait Operation
case object Buy  extends Operation
case object Sell extends Operation
