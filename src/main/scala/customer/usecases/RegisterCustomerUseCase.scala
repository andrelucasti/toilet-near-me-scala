package io.andrelucas
package customer

import customer.RegisterCustomerUseCase.{Input, Output}

import io.andrelucas.customer.usecases.CustomerRepository

import scala.concurrent.Future
import scala.util.Try

case class RegisterCustomerUseCase(repository: CustomerRepository):
  def execute(input: Input): Future[Output] =
    Try {
      Customer.newCustomer(input.name, input.email, input.password)
    }.map(c => c).fold(
      exception => Future.failed(exception),
      customer => Future.successful {
        repository.save(customer)
        Output(customer.customerId)
      }
    )

case object RegisterCustomerUseCase:
  case class Input(name: String, email: String, password: String)
  case class Output(customerId: CustomerId)