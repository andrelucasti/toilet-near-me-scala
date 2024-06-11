package io.andrelucas
package customer

import io.andrelucas.application.customer.Customer
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class CustomerTest extends AnyFlatSpecLike with Matchers {

  it should "create a new customer" in {
    Customer.newCustomer("Andre Lucas","andrelucastic@gmail.com", "123456")
      .map { customer =>
        customer.name should be("Andre Lucas")
        customer.email should be("andrelucastic@gmail.com")
        customer.password should be("123456")
      }

    for {
      customer <- Customer.newCustomer("Andre Lucas","andrelucastic@gmail.com", "123456")
    } yield {
      customer.name should be("Andre Lucas")
      customer.email should be("andrelucastic@gmail.com")
      customer.password should be("123456")
    }
  }

  it should "not create a new customer with empty name" in {
    Customer.newCustomer("","andrelucastic@gmail.com", "123456")
      .failed.map { exception =>
        exception.getMessage should be("Name cannot be empty")
      }
  }

  it should "not create a new customer with empty email" in {
    Customer.newCustomer("Andre Lucas", "", "123456")
      .failed.map { exception =>
        exception.getMessage should be("Email cannot be empty")
      }
  }

  it should "not create a new customer when email is invalid" in {
    Customer.newCustomer("Andre Lucas", "andrelucastic.com", "123456")
      .failed.map { exception =>
        exception.getMessage should be("Invalid email")
      }
  }
}
