# What

This is a (somewhat) completed solution for a provided code challenge of a
Capital Gains tax calculator.

# Notes

This is a Scala application, using [Cats Effect](https://typelevel.org/cats-effect/) [effect system](https://en.wikipedia.org/wiki/Effect_system), alongside with some other related libraries to handle relevant parts of functionality, namely [Circe](https://circe.github.io/circe/) for JSON and [FS2](https://fs2.io/#/) for input manipulation.

To comply with the generic approach of this ecosystem, I separated the application itself that runs with a side-effects (input/output) and the logic behind calculations. Thus integration tests are not included, since there is no point to test 3d-party library for correctness.

# How to build

## If you don't have a [sbt](https://www.scala-sbt.org/download.html) tool in your system

Follow any instruction to install it onto you system: 

* with [Coursier](https://get-coursier.io/docs/cli-installation) - ```cs install sbt```
* with [SDKMan](https://sdkman.io/install) - ```sdk install sbt```
* with Homebrew - ```brew install sbt```
* or any other way suitable to you

## After you already have `sbt` installed

* ```sbt test``` - runs unit tests packed with the solution
* ```sbt assembly``` - builds an executable jar in `<working directory>/target/scala.../` directory
* ```java -jar <path-to-jar>/nubankchallenge.jar < <input file>``` - runs the application against an input file provided by you

# Personal notes

I'd like to note that I left last (#9) test case failing, since at this point this challenge became an exercise in a business logic reverse engineering and just a sink for the time I don't have. Thus that challenge isn't 100% done from that point of view, obviously.
Besides that, this project contains most of the bits and bolts that are expected to be found in a production-ready Scala (!) application, even if not all of them are finalized to be work together.
