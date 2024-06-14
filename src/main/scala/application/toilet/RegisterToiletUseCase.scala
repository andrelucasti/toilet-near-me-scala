package io.andrelucas
package application.toilet

import application.toilet.RegisterToiletUseCase.{Input, Output}

import io.andrelucas.application.customer.{CustomerId, CustomerNotFoundException}

import java.util.UUID
import scala.concurrent.{ExecutionContext, Future}

case class RegisterToiletUseCase()(implicit ec: ExecutionContext):
  def execute(input: Input, 
              customerExist: CustomerId => Boolean,
              registerToilet: (Toilet) => Future[Unit]): Future[Output] = 
    
  if !customerExist(CustomerId(input.customerId)) then  
    Future.failed(CustomerNotFoundException(s"Customer ${input.customerId} does not exist"))
  else 
    Future.fromTry {
      Toilet.create(input.toiletName, input.latitude, input.longitude)
    }.map { toilet =>
      registerToilet(toilet)
      Output(toilet.id.asString)
    }.recoverWith { e => 
      Future.failed(e)
    }
  
    
    
case object RegisterToiletUseCase:
  
  case class Input(toiletName: String, 
                   latitude: Double, 
                   longitude: Double, 
                   customerId: UUID)
  
  case class Output(toiletId: String)
