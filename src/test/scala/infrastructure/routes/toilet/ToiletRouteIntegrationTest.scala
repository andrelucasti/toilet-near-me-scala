package io.andrelucas
package infrastructure.routes.toilet

import infrastructure.db.toilet.ToiletInMemoryRepository
import infrastructure.routes.db.CustomerRepositoryInMemory

import io.andrelucas.application.customer.{Customer, CustomerId}
import io.opentelemetry.api.GlobalOpenTelemetry
import org.apache.pekko.http.scaladsl.model.{ContentTypes, HttpEntity}
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class ToiletRouteIntegrationTest extends AnyFlatSpecLike
  with Matchers
  with ScalatestRouteTest {

  private val customerRepository = new CustomerRepositoryInMemory()
  private val toiletRepository = new ToiletInMemoryRepository()
  

  it should "return 400 when creating a new toilet with invalid name" in {

    val customerId = Customer.create("André Lucas", "andrelucastic@gmail.com", "123456")
      .map(t => {
        customerRepository.save(t)
        t.id
      }).get

    val payload =
      s"""{"name": "",
        |"latitude": 38.714569257162864,
        |"longitude": -9.12229635945829,
        |"price": 50,
        |"customerId": "${customerId.value.toString}"
        |}""".stripMargin

    val requestEntity = HttpEntity(ContentTypes.`application/json`, payload)
    val path = "/toilet"
    Post(path, requestEntity) ~> ToiletRoute(toiletRepository, customerRepository, GlobalOpenTelemetry.get()) ~> check {
      status.intValue shouldEqual 400
    }
  }

  it should "return 400 when creating a new toilet with customer does not exist" in {
    val customerId = CustomerId.generate

    val payload =
      s"""{"name": "Toilet",
        |"latitude": 38.714569257162864,
        |"longitude": -9.12229635945829,
        |"price": 50,
        |"customerId":"${customerId.value.toString}"
        |}""".stripMargin

    val requestEntity = HttpEntity(ContentTypes.`application/json`, payload)
    val path = s"/toilet"
    Post(path, requestEntity) ~> ToiletRoute(toiletRepository, customerRepository, GlobalOpenTelemetry.get()) ~> check {
      status.intValue shouldEqual 400
    }
  }

  it should "return 400 when creating a new toilet with geolocation out of range" in {
    val customerId = Customer.create("André Lucas", "andrelucastic@gmail.com", "123456")
      .map(t => {
        customerRepository.save(t)
        t.id
      }).get

    val payload =
      s"""{"name": "Toilet",
        |"latitude": 97,
        |"longitude": 182,
        |"price": 50,
        |"customerId":"${customerId.value.toString}"
        |}""".stripMargin

    val requestEntity = HttpEntity(ContentTypes.`application/json`, payload)
    val path = "/toilet"
    Post(path, requestEntity) ~> ToiletRoute(toiletRepository, customerRepository, GlobalOpenTelemetry.get()) ~> check {
      status.intValue shouldEqual 400
    }
  }

  it should "return 201 when creating a new toilet" in {
    val customerId = Customer.create("André Lucas", "andrelucastic@gmail.com", "123456")
      .map(t => {
        customerRepository.save(t)
        t.id
      }).get

    val payload =
      s"""{"name": "Toilet",
        |"latitude": 38.714569257162864,
        |"longitude": -9.12229635945829,
        |"price": 50,
        |"customerId": "${customerId.value.toString}"
        |}""".stripMargin

    val requestEntity = HttpEntity(ContentTypes.`application/json`, payload)
    val path = "/toilet"
    Post(path, requestEntity) ~> ToiletRoute(toiletRepository, customerRepository, GlobalOpenTelemetry.get()) ~> check {
      status.intValue shouldEqual 201
    }
  }
}
