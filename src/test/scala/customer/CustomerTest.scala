package io.andrelucas
package customer

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers

class CustomerTest extends AnyFlatSpecLike with Matchers {

  it should "create a new customer" in {
    val customer = Customer.newCustomer("Andre Lucas","andrelucastic@gmail.com", "123456")
    customer.name should be("Andre Lucas")
    customer.email should be ("andrelucastic@gmail.com")
    customer.password should be ("123456")
  }

  it should "not create a new customer with empty name" in {
    a [NameInvalidException] should be thrownBy Customer.newCustomer("","andrelucastic@gmail.com", "123456")
  }

  it should "not create a new customer with empty email" in {
    a[EmailInvalidException] should be thrownBy Customer.newCustomer("Andre Lucas", "", "123456")
  }

  it should "not create a new customer when email is invalid" in {
    a[EmailInvalidException] should be thrownBy Customer.newCustomer("Andre Lucas", "andrelucas.com", "123456")
  }
}
