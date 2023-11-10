import sbt.Keys._
import D._

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / organization := "dev.nubank.challenge"

lazy val root = project
  .in(file("."))
  .settings(
    name := "challenge",
    libraryDependencies :=
      conf
      ++ cats
      ++ circe
      ++ logging
  )
