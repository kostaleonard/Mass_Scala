name := "Mass_Scala"

version := "1.0"

scalaVersion := "2.12.2"

mainClass in (Compile, packageBin) := Some("controller.Controller")

mainClass in (Compile, run) := Some("controller.Controller")

connectInput in run := true

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.1"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"