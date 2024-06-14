package io.andrelucas
package infrastructure.routes.db

import application.customer.repository.CustomerRepository
import application.customer.{Customer, CustomerId}

import scala.concurrent.Future

class CustomerRepositoryInMemory extends CustomerRepository {
  private val buffer = scala.collection.mutable.Buffer.empty[Customer]

  override def save(customer: Customer): Future[Unit] = {
    buffer += customer
    println(s"Saving customer: $customer")
    Future.successful(())
  }

  override def findById(id: CustomerId): Future[Option[Customer]] = {
    println(s"Finding customer by id: $id")
    Future.successful(buffer.find(_.id == id))
  }

  override def findAll(): Future[Seq[Customer]] = {
    println(s"Finding all customers")
    Future.successful(buffer.toSeq)
  }

  override def delete(id: CustomerId): Future[Unit] = {
    println(s"Deleting customer by id: $id")
    buffer --= buffer.filter(_.id == id)
    Future.successful(())
  }

  override def update(customer: Customer): Future[Unit] = {
    println(s"Updating customer: $customer")
    buffer --= buffer.filter(_.id == customer.id)
    buffer += customer
    Future.successful(())
  }
  
  override def exist(customerId: CustomerId): Future[Boolean] = {
    Future.successful(buffer.exists(_.id == customerId))
  }
}
