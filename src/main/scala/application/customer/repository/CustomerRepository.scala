package io.andrelucas
package application.customer.repository

import io.andrelucas.application.commons.Repository
import io.andrelucas.application.customer.{Customer, CustomerId}

import scala.concurrent.Future

trait CustomerRepository extends Repository[Customer, CustomerId]:
  def exist(customerId: CustomerId): Future[Boolean]


