package io.andrelucas
package infrastructure.routes.customer

import application.commons.{EmailInvalidException, NameInvalidException}
import application.customer.repository.CustomerRepository
import application.customer.usecases.RegisterCustomerUseCase

import com.typesafe.scalalogging.Logger
import org.apache.pekko
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import spray.json.DefaultJsonProtocol.{StringJsonFormat, jsonFormat2}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

import scala.concurrent.ExecutionContext

class CustomerRoute

object CustomerRoute  {
  private val logger = Logger("CustomerRoute")
  private def getAllCustomers(customerRepository: CustomerRepository): Route = {
    get {
      complete(s"Customer")
    }
  }

  private def createCustomer(customerRepository: CustomerRepository)(implicit ec: ExecutionContext): Route = {
    post {
      entity(as[CreateCustomerRequest]) { request =>
        val input = RegisterCustomerUseCase.Input(request.name, request.email, customerRepository.save, () => "123456")
        val output = RegisterCustomerUseCase().execute(input)
        
        onComplete(output) {
          case scala.util.Success(output) => complete(StatusCodes.Created, s"Customer created with id: ${output.customerId}")
          case scala.util.Failure(exception: EmailInvalidException) =>
            logger.error(exception.getMessage)
            complete(StatusCodes.BadRequest, s"Error: ${exception.getMessage}")
          case scala.util.Failure(exception: NameInvalidException) =>
            logger.error(exception.getMessage)
            complete(StatusCodes.BadRequest, s"Error: ${exception.getMessage}")
          case scala.util.Failure(exception) =>
            logger.error(exception.getMessage)
            complete(StatusCodes.InternalServerError, s"Error: ${exception.getMessage}")
        }
      }
    }
  }

  private def getCustomerById(customerRepository: CustomerRepository, id: String): Route = {
    get {
      complete(s"Customer with id: $id")
    }
  }

  def apply(customerRepository: CustomerRepository)(implicit ec: ExecutionContext): Route =
    pathPrefix("customers") {
      concat(
        pathEnd {
          concat(
            getAllCustomers(customerRepository),
            createCustomer(customerRepository)
          )
        },
        path(Segment) { id =>
          concat(
            getCustomerById(customerRepository, id)
          )
        }
      )
  }

  case class CreateCustomerRequest(name: String, email: String)
  given createCustomerRequestFormat: RootJsonFormat[CreateCustomerRequest] = DefaultJsonProtocol.jsonFormat2(CreateCustomerRequest.apply)
}


