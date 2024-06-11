package io.andrelucas
package application.customer.repository

import io.andrelucas.application.customer.Customer

import scala.concurrent.Future

trait CustomerRepository {
  def save(customer: Customer): Future[Unit]

}
