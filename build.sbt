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
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.6"
libraryDependencies += "org.postgresql" % "postgresql" % pgVersion

libraryDependencies += "org.awaitility" % "awaitility-scala" % "4.2.0" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % Test
libraryDependencies += "org.scalatestplus" %% "mockito-5-10" % scalaMockitoVersion % Test

val PekkoVersion = "1.0.2"
val PekkoHttpVersion = "1.0.1"
val slickVersion = "3.5.1"

libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-actor-typed" % PekkoVersion,
  "org.apache.pekko" %% "pekko-stream" % PekkoVersion,
  "org.apache.pekko" %% "pekko-http" % PekkoHttpVersion,
  "org.apache.pekko" %% "pekko-http-spray-json" % PekkoHttpVersion,

  "org.apache.pekko" %% "pekko-stream-testkit" % PekkoVersion,
  "org.apache.pekko" %% "pekko-http-testkit" % PekkoHttpVersion,

  "com.typesafe.slick" %% "slick" % slickVersion,
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.slick" %% "slick-testkit" % slickVersion,

  "org.postgresql" % "postgresql" % pgVersion,

  "org.flywaydb" % "flyway-core" % "9.0.0"
)
libraryDependencies += "org.scala-lang" %% "toolkit-test" % "0.1.7" % Test

val openTelemetryVersion = "1.39.0"
val openTelemetryAppenderLogVersion = "2.4.0-alpha"

libraryDependencies ++= Seq(
  "io.opentelemetry" % "opentelemetry-api" % openTelemetryVersion,
  "io.opentelemetry" % "opentelemetry-sdk" % openTelemetryVersion,
  "io.opentelemetry" % "opentelemetry-exporter-otlp" % openTelemetryVersion,
  "io.opentelemetry.semconv" % "opentelemetry-semconv" % "1.25.0-alpha",
  "io.opentelemetry" % "opentelemetry-sdk-extension-autoconfigure" % openTelemetryVersion,

  "io.opentelemetry.instrumentation" % "opentelemetry-log4j-appender-2.17" % openTelemetryAppenderLogVersion,
  "io.opentelemetry.instrumentation" % "opentelemetry-logback-appender-1.0" % openTelemetryAppenderLogVersion,
)

addCommandAlias("migration", ";clean;compile;runMain io.andrelucas.infrastructure.AppMigration")
addCommandAlias("startMinimalApp", ";clean;compile;runMain io.andrelucas.AppMinimal")