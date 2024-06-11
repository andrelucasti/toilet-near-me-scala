package io.andrelucas
package infrastructure.routes.customer.db

import application.customer.Customer
import application.customer.repository.CustomerRepository

import com.typesafe.scalalogging.Logger

import scala.concurrent.Future

class CustomerRepositoryInMemory extends CustomerRepository {
  private val buffer = scala.collection.mutable.Buffer.empty[Customer]

  override def save(customer: Customer): Future[Unit] = {
    buffer += customer
    println(s"Saving customer: $customer")
    Future.successful(())
  }
}

case object CustomerRepositoryInMemory {}
