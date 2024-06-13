package io.andrelucas
package application.customer.repository

import io.andrelucas.application.commons.Repository
import io.andrelucas.application.customer.{Customer, CustomerId}

import scala.concurrent.Future

trait CustomerRepository extends Repository[Customer, CustomerId]:
  def save(customer: Customer): Future[Unit]
  def findById(id: CustomerId): Future[Option[Customer]]
  def findAll(): Future[Seq[Customer]]
  def delete(id: CustomerId): Future[Unit]
  def update(customer: Customer): Future[Unit]


