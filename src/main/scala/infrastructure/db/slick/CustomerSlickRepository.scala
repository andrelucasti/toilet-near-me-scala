package io.andrelucas
package infrastructure.db.slick

import application.customer.repository.CustomerRepository
import application.customer.{Customer, CustomerId}

import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api.*

import java.time.LocalDateTime
import scala.concurrent.{ExecutionContext, Future}

class CustomerSlickRepository(db: Database)(implicit ec: ExecutionContext) extends CustomerRepository {
  private val customers = TableQuery[CustomerTable]

  override def save(customer: Customer): Future[Unit] =
    db.run(customers += (customer.id.value, customer.name, customer.email, customer.password, 0, LocalDateTime.now())).map(_ => ())

  override def findAll(): Future[Seq[Customer]] =
    db.run(customers.result).map(_.map {
      case (id, name, email, password, version, updatedAt) => Customer(CustomerId(id), name, email, password)
    })

  override def update(customer: Customer): Future[Unit] =
    val action = for {
      currentVersion <- customers.filter(_.id === customer.id.value).map(_.version).result.head

      _ <- customers.filter(_.id === customer.id.value)
        .filter(_.version === currentVersion)
        .map(c => (c.name, c.email, c.password, c.version, c.updatedAt))
        .update((customer.name, customer.email, customer.password, currentVersion + 1, LocalDateTime.now()))
    } yield ()

    db.run(action.transactionally)

  override def findById(id: CustomerId): Future[Option[Customer]] =
    db.run(customers.filter(_.id === id.value).result.headOption).map(_.map {
      case (id, name, email, password, version, updatedAt) => Customer(CustomerId(id), name, email, password)
    })

  override def delete(id: CustomerId): Future[Unit] =
    db.run(customers.filter(_.id === id.value).delete).map(_ => ())

}
