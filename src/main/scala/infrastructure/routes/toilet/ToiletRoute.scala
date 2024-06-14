package io.andrelucas
package infrastructure.routes.toilet


import application.customer.{CustomerId, CustomerNotFoundException}
import application.customer.repository.CustomerRepository
import application.toilet.{GeolocationException, RegisterToiletUseCase, ToiletRepository}

import com.typesafe.scalalogging.Logger
import io.andrelucas.application.commons.NameInvalidException
import io.andrelucas.infrastructure.routes.customer.CustomerRoute.getCustomerById
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko
import org.apache.pekko.http.scaladsl.model.StatusCodes
import org.apache.pekko.http.scaladsl.marshallers.sprayjson.SprayJsonSupport.*
import org.apache.pekko.http.scaladsl.server.Directives.*
import spray.json.DefaultJsonProtocol.*
import spray.json.*
import org.apache.pekko.http.scaladsl.server.directives.*

import java.util.UUID
import scala.concurrent.ExecutionContext


class ToiletRoute 

object ToiletRoute {
  private val logger = Logger("ToiletRoute")
  
  private def createToilet(toiletRepository: ToiletRepository, 
                           customerRepository: CustomerRepository,
                           customerId: String)(implicit ec: ExecutionContext): Route = {
    post {
      entity(as[CreateToiletRequest]) { request =>
        val customerUUID = CustomerId(UUID.fromString(customerId))
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

    pathPrefix("toilets") {
      concat(
        path(Segment) { customerId =>
          concat(
            createToilet(toiletRepository, customerRepository, customerId)
          )
        }
      )
    }
    
    
    
  case class CreateToiletRequest(name: String,
                                 latitude: Double,
                                 longitude: Double,
                                 price: Long)
  
  given creatToiletRequestFormat: RootJsonFormat[CreateToiletRequest] = jsonFormat4(CreateToiletRequest.apply)
}
