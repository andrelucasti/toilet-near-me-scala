package io.andrelucas
package infrastructure.routes.customer


import io.andrelucas.infrastructure.routes.db.CustomerRepositoryInMemory
import org.apache.pekko.http.scaladsl.model.{ContentTypes, HttpEntity}
import org.apache.pekko.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class CustomerRouteIntegrationTest extends AnyFlatSpecLike
  with Matchers
  with ScalatestRouteTest {

  private val customerRepository = new CustomerRepositoryInMemory()
  
  it should "return 201 when creating a new customer" in {
    val requestEntity = HttpEntity(ContentTypes.`application/json`, """{"name": "John Doe", "email": "john.doe@example.com"}""")
    Post("/customers", requestEntity) ~> CustomerRoute(customerRepository) ~> check {
      status.intValue shouldEqual 201
    }
  }

  it should "return 400 when creating a new customer with invalid email" in {
    val requestEntity = HttpEntity(ContentTypes.`application/json`, """{"name": "John Doe", "email": "john.doe"}""")
    Post("/customers", requestEntity) ~> CustomerRoute(customerRepository) ~> check {
      status.intValue shouldEqual 400
    }
  }

  it should "return 400 when creating a new customer with empty name" in {
    val requestEntity = HttpEntity(ContentTypes.`application/json`, """{"name": "", "email": "john.doe"}""")
    Post("/customers", requestEntity) ~> CustomerRoute(customerRepository) ~> check {
      status.intValue shouldEqual 400
    }
  }
}
