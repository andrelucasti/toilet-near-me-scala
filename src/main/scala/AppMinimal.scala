package io.andrelucas

import infrastructure.db.slick.CustomerSlickRepository
import infrastructure.routes.customer.CustomerRoute

import org.apache.pekko
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.Http
import slick.jdbc.JdbcBackend.Database

import java.util.concurrent.Executors
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor}
import scala.io.StdIn

object AppMinimal {
  given system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "Minimal")
  given execution: scala.concurrent.ExecutionContext = system.executionContext
  private val virtualThreadExecutor: ExecutionContextExecutor = ExecutionContext.fromExecutor(Executors.newVirtualThreadPerTaskExecutor())

  val db = Database.forConfig("toiletdb")
  val customerRepository = new CustomerSlickRepository(db)(virtualThreadExecutor)

  def main(args: Array[String]): Unit = {
    val binding = Http().newServerAt("localhost", 8081).bind(CustomerRoute(customerRepository)(virtualThreadExecutor))

    println(s"Server online at http://localhost:8081/")

    StdIn.readLine()
    binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}
