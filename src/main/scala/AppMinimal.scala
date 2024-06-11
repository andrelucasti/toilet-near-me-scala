package io.andrelucas

import infrastructure.routes.customer.CustomerRoute

import io.andrelucas.application.customer.Customer
import io.andrelucas.application.customer.repository.CustomerRepository
import org.apache.pekko
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.Http

import scala.concurrent.Future
import scala.io.StdIn

object AppMinimal {
  private val customerRepository = new CustomerRepository {
    override def save(customer: Customer): Future[Unit] = {
      println(s"Saving customer: $customer")
      Future.successful(())
    }
  }
  
  given system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Minimal")
  given execution: scala.concurrent.ExecutionContext = system.executionContext


  def main(args: Array[String]): Unit = {

    val binding = Http().newServerAt("localhost", 8081).bind(CustomerRoute(customerRepository))

    println(s"Server online at http://localhost:8081/")

    StdIn.readLine()
    binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}
