package io.andrelucas
package infrastructure.otel

import com.typesafe.scalalogging.Logger
import io.opentelemetry.context.Context


case object OtelStarterTest:
  private val logger = Logger("OtelStarterTest")

  def main(args: Array[String]): Unit = {
    logger.info("Hello, OtelStarterTest!")

    val openTelemetry = OtelConfiguration()
    val trace = openTelemetry.getTracer("io.andrelucas.trace.example")
    val meter = openTelemetry.getMeter("io.andrelucas.meter.example")

    val span = trace.spanBuilder("example-span").startSpan()
    span.setAttribute("example-attribute", "example-value")

    Context.current().`with`(span).makeCurrent()

    Thread.sleep(2000)
    span.end()

    logger.info("This is an info log message.")
    logger.warn("This is a warning log message.")
    logger.error("This is an error log message.")
    
  }
