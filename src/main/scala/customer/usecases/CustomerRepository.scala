package io.andrelucas
package customer.usecases

import io.andrelucas.customer.Customer

import scala.concurrent.Future

trait CustomerRepository {
  def save(customer: Customer): Future[Unit]

}
