package io.andrelucas
package infrastructure.otel

import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.exporter.otlp.metrics.OtlpGrpcMetricExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.sdk.logs.`export`.BatchLogRecordProcessor
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.metrics.`export`.PeriodicMetricReader
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.`export`.BatchSpanProcessor
import io.opentelemetry.semconv.ResourceAttributes.SERVICE_NAME

import java.util.concurrent.TimeUnit


case object OtelConfiguration:
  def apply(): OpenTelemetry =
    val resource = Resource.getDefault
      .merge(Resource.builder().put(SERVICE_NAME, "toilet-near-me").build())

    val otelSdk =  OpenTelemetrySdk.builder()
      .setTracerProvider(
        SdkTracerProvider.builder()
          .setResource(resource)
          .addSpanProcessor(
            BatchSpanProcessor.builder(
              OtlpGrpcSpanExporter.builder()
                .setTimeout(2, TimeUnit.SECONDS)
                .build())
            .setScheduleDelay(100, TimeUnit.MILLISECONDS)
              .build())
          .build())
      .setMeterProvider(
        SdkMeterProvider.builder()
          .setResource(resource)
          .registerMetricReader(
            PeriodicMetricReader.builder(OtlpGrpcMetricExporter.getDefault)
              .setInterval(1, TimeUnit.SECONDS)
              .build())
          .build())
      .setLoggerProvider(
        SdkLoggerProvider.builder()
          .setResource(resource)
          .addLogRecordProcessor(
            BatchLogRecordProcessor.builder(
              OtlpGrpcLogRecordExporter.builder()
                .setTimeout(2, TimeUnit.SECONDS)
                .build())
            .setScheduleDelay(100, TimeUnit.MILLISECONDS)
            .build())
          .build())
      .buildAndRegisterGlobal()

    Runtime.getRuntime.addShutdownHook(new Thread(() => otelSdk.close()))
    otelSdk


