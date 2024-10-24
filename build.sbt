name := "scala-unicode"

version := "0.1.0"

scalaVersion := "2.13.15"

Compile / publishArtifact := false

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.19" % Test // not using
)
