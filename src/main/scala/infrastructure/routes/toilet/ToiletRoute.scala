package io.andrelucas
package infrastructure.routes.toilet


import application.commons.NameInvalidException
import application.customer.repository.CustomerRepository
import application.customer.{CustomerId, CustomerNotFoundException}
import application.toilet.{GeolocationException, RegisterToiletUseCase, ToiletRepository}

import com.typesafe.scalalogging.Logger
import org.apache.pekko
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.server.Directives.*
import org.apache.pekko.http.scaladsl.server.Route
import spray.json.*
import spray.json.DefaultJsonProtocol.*

import java.util.UUID
import scala.concurrent.ExecutionContext


class ToiletRoute 

object ToiletRoute {
  private val logger = Logger("ToiletRoute")
  
  private def createToilet(toiletRepository: ToiletRepository, 
                           customerRepository: CustomerRepository)(implicit ec: ExecutionContext): Route = {
    post {
      entity(as[CreateToiletRequest]) { request =>
        val customerUUID = CustomerId(UUID.fromString(request.customerId))
        val input = RegisterToiletUseCase.Input(request.name, request.latitude, request.longitude, customerUUID.value, request.price)
        val output = RegisterToiletUseCase().execute(input, customerRepository.exist, toiletRepository.save)
        
        onComplete(output) {
          case scala.util.Success(output) => complete(StatusCodes.Created, s"Toilet created with id: ${output.toiletId}")
          case scala.util.Failure(exception: CustomerNotFoundException) =>
            logger.error(exception.getMessage)
            complete(StatusCodes.BadRequest, s"Error: ${exception.getMessage}")
          case scala.util.Failure(exception: NameInvalidException) =>
            logger.error(exception.getMessage)
            complete(StatusCodes.BadRequest, s"Error: ${exception.getMessage}")
          case scala.util.Failure(exception: GeolocationException) =>
            logger.error(exception.getMessage)
            complete(StatusCodes.BadRequest, s"Error: ${exception.getMessage}")
          case scala.util.Failure(exception) =>
            logger.error(exception.getMessage)
            complete(StatusCodes.InternalServerError, "Unexpected error. Please try again later.")
        }
      }
    }
  }
  
  def apply(toiletRepository: ToiletRepository,
            customerRepository: CustomerRepository)(implicit ec: ExecutionContext): Route =

    path("toilet") {
      concat(
        createToilet(toiletRepository, customerRepository),
      )
    }

  case class CreateToiletRequest(name: String,
                                 latitude: Double,
                                 longitude: Double,
                                 price: Long,
                                 customerId: String)
  
  given creatToiletRequestFormat: RootJsonFormat[CreateToiletRequest] = jsonFormat5(CreateToiletRequest.apply)
}
