package io.andrelucas
package application.commons

import scala.concurrent.Future

trait Repository[A, ID]:
  def save(entity: A): Future[Unit]
  def findById(id: ID): Future[Option[A]]
  def findAll(): Future[Seq[A]]
  def delete(id: ID): Future[Unit]
  def update(entity: A): Future[Unit]

