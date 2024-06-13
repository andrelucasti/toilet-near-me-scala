package io.andrelucas
package application.customer.usecases

import application.customer.usecases.RegisterCustomerUseCase.{Input, Output}
import application.customer.{Customer, CustomerId}

import scala.concurrent.{ExecutionContext, Future}

case class RegisterCustomerUseCase() (implicit ec: ExecutionContext):

  def execute(input: Input): Future[Output] =
    Future.fromTry {
      Customer.create(input.name, input.email, input.passwordGenerator())
    }.flatMap { customer => input.saveCustomer(customer).map(_ => Output(customer.id))
    }.recoverWith {
      case exception =>
        println(exception.getMessage)
        Future.failed(exception)
    }


case object RegisterCustomerUseCase:

  case class Input(name: String, email: String,
                   saveCustomer: Customer => Future[Unit],
                   passwordGenerator: () => String)

  case class Output(customerId: CustomerId)