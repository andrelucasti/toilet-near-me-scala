package io.andrelucas
package customer

import customer.RegisterCustomerUseCase.{Input, Output}

import io.andrelucas.customer.usecases.CustomerRepository

import scala.concurrent.Future
import scala.util.{Try, Success, Failure}
import scala.concurrent.ExecutionContext.Implicits.global

case class RegisterCustomerUseCase():

  def execute(input: Input, saveCustomer: Customer => Future[Unit]): Future[Output] =
    Future.fromTry {
      Try{ Customer.newCustomer(input.name, input.email, input.password) }
    }.flatMap {
      case Success(customer) =>
       Future.successful {
         saveCustomer(customer)
         Output(customer.customerId)
       }
      case Failure(exception) =>
        Future.failed(exception)
    }
//    Try {
//      Customer.newCustomer(input.name, input.email, input.password)
//    }.map(c => c).fold(
//      exception => Future.failed(exception),
//      customer => Future.successful {
//        repository.save(customer)
//        Output(customer.customerId)
//      }
//    )

case object RegisterCustomerUseCase:
  case class Input(name: String, email: String, password: String)
  case class Output(customerId: CustomerId)