package io.andrelucas
package infrastructure.db.slick

import slick.jdbc.PostgresProfile.api._


import java.util.UUID

type CustomerType = (UUID, String, String, String, Int, java.time.LocalDateTime)
class CustomerTable(tag: Tag) extends Table[CustomerType](tag, "customers"){
  def id = column[UUID]("id", O.SqlType("UUID"))
  def name = column[String]("name")
  def email = column[String]("email")
  def password = column[String]("password")
  def version = column[Int]("version")
  def updatedAt = column[java.time.LocalDateTime]("updated_at", O.Default(java.time.LocalDateTime.now()))
  
  override def * = (id, name, email, password, version, updatedAt)
}
