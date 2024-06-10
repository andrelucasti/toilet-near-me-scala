package io.andrelucas
package customer

import scala.util.{Success, Try}

case class Customer(customerId: CustomerId,
                    name: String,
                    email: String,
                    password: String)



case object Customer:

  def newCustomer(name: String,
                  email: String,
                  password: String): Try[Customer] =
    Try {
      val emailRegex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$".r

      if (name.isEmpty) throw new NameInvalidException("Name cannot be empty")
      if (email.isEmpty) throw new EmailInvalidException("Email cannot be empty")
      if (!emailRegex.matches(email)) throw new EmailInvalidException("Invalid email")
      
      Customer(CustomerId.generate, name, email, password)
    }