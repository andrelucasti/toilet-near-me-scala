ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.4.2"

lazy val root = (project in file("."))
  .settings(
    name := "toilet-near-me",
    idePackagePrefix := Some("io.andrelucas")
  )

lazy val scalaLoggingVersion = "3.9.5"
lazy val pgVersion = "42.7.3"
lazy val scalaTestVersion = "3.2.18"
lazy val scalaMockitoVersion = "3.2.18.0"

libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
libraryDependencies += "org.postgresql" % "postgresql" % pgVersion

libraryDependencies += "org.awaitility" % "awaitility-scala" % "4.2.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % Test
libraryDependencies += "org.scalatestplus" %% "mockito-5-10" % scalaMockitoVersion % Test
