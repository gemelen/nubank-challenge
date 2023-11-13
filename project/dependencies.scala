import sbt._

object V {
  val cats    = "2.10.0"
  val ce      = "3.5.2"
  val fs2     = "3.9.3"
  val l4c     = "2.6.0"
  val logback = "1.4.11"
  val circe   = "0.14.6"
  val config  = "1.4.3"
  val slf4j   = "2.0.9"
}

object VT {
  val munit = "1.0.0-M10"
  val muce  = "2.0.0-M4"
}

object D {
  val conf = Seq(
    "com.typesafe" % "config" % V.config
  )

  val cats = Seq(
    "org.typelevel" %% "cats-core"   % V.cats,
    "org.typelevel" %% "cats-effect" % V.ce
  )

  val fs2 = Seq(
    "co.fs2" %% "fs2-core",
    "co.fs2" %% "fs2-io"
  ).map(_ % V.fs2)

  val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
    "io.circe" %% "circe-parser"
  ).map(_ % V.circe)

  val logging = Seq(
    "org.typelevel" %% "log4cats-slf4j"  % V.l4c,
    "org.slf4j"      % "slf4j-api"       % V.slf4j,
    "ch.qos.logback" % "logback-classic" % V.logback
  )

  val munit = Seq(
    "org.scalameta" %% "munit"             % VT.munit,
    "org.typelevel" %% "munit-cats-effect" % VT.muce
  ).map(_ % Test)

  val tests = munit
}
