name := "scala-unicode"

version := "0.1.0"

scalaVersion := "3.9.0"

(Compile / publishArtifact) := false

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.20" % Test
)
