name := "scala-unicode"

version := "0.1.0"

scalaVersion := "2.12.8"

publishArtifact in Compile := false

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.5" % Test
)
