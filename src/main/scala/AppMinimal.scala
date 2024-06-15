package io.andrelucas

import infrastructure.db.slick.CustomerSlickRepository
import infrastructure.routes.customer.CustomerRoute

import io.andrelucas.infrastructure.db.slick.toilet.ToiletSlickRepository
import io.andrelucas.infrastructure.routes.toilet.ToiletRoute
import org.apache.pekko
import org.apache.pekko.actor.typed.ActorSystem
import org.apache.pekko.actor.typed.scaladsl.Behaviors
import org.apache.pekko.http.scaladsl.Http
import org.apache.pekko.http.scaladsl.server.Directives
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
  val toiletRepository = new ToiletSlickRepository(db)(virtualThreadExecutor)

  def main(args: Array[String]): Unit = {
    val routes = Directives.concat(
      CustomerRoute(customerRepository),
      ToiletRoute(toiletRepository, customerRepository)
    )
    
    val binding = Http().newServerAt("localhost", 8081).bind(routes)

    println(s"Server online at http://localhost:8081/")

    StdIn.readLine()
    binding.flatMap(_.unbind()).onComplete(_ => system.terminate())
  }
}
