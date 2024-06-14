package io.andrelucas
package application.toilet

import application.customer.{CustomerId, CustomerNotFoundException}
import application.toilet.RegisterToiletUseCase.{Input, Output}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class RegisterToiletUseCase()(implicit ec: ExecutionContext):
  def execute(input: Input,
              customerExist: CustomerId => Future[Boolean],
              registerToilet: Toilet => Future[Unit]): Future[Output] =

    customerExist(CustomerId(input.customerId))
      .flatMap {
        case false => Future.failed(CustomerNotFoundException(s"Customer ${input.customerId} does not exist"))
        case true =>
          Toilet.create(input.toiletName, input.latitude, input.longitude) match
            case Failure(exception) => Future.failed(exception)
            case Success(toilet) => registerToilet(toilet).map(_ => Output(toilet.id.asString))
      }

case object RegisterToiletUseCase:

  case class Input(toiletName: String,
                   latitude: Double,
                   longitude: Double,
                   customerId: UUID)

  case class Output(toiletId: String)
