import D._
import sbt.Keys._

ThisBuild / scalaVersion := "3.3.1"
ThisBuild / organization := "dev.nubank.challenge"

ThisBuild / semanticdbEnabled := true
ThisBuild / scalacOptions ++=
  Seq(
    "-encoding", "utf8",
    "-deprecation",
    "-no-indent",
    "-Werror",
    "-Wunused:all",
    "-Wvalue-discard"
  )

ThisBuild / assemblyMergeStrategy := {
  case "module-info.class" => MergeStrategy.concat
  case x =>
    val old = (ThisBuild / assemblyMergeStrategy).value(_)
    old(x)
}

lazy val root = project
  .in(file("."))
  .settings(
    name := "challenge",
    libraryDependencies :=
      conf
        ++ cats
        ++ circe
        ++ fs2
        ++ logging
  )
