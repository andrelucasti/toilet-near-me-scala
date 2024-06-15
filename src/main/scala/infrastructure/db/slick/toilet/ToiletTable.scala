package io.andrelucas
package infrastructure.db.slick.toilet

import io.andrelucas.application.toilet.Toilet
import slick.jdbc.PostgresProfile.api.*

import java.util.UUID

type ToiletType = (UUID, String, String, Long, Int, java.time.LocalDateTime)
class ToiletTable(tag: Tag) extends Table[ToiletType](tag, "toilets") {
  def id = column[UUID]("id", O.SqlType("UUID"))
  def name = column[String]("name")
  def toiletType = column[String]("type")
  def price = column[Long]("price")
  def version = column[Int]("version")
  def updatedAt = column[java.time.LocalDateTime]("updated_at", O.Default(java.time.LocalDateTime.now()))

  override def * = (id, name, toiletType, price, version, updatedAt)
}

type ToiletGeolocationType = (UUID, Double, Double, Int, java.time.LocalDateTime)
class ToiletGeolocationTable(tag: Tag) extends Table[ToiletGeolocationType](tag, "toilet_geolocation") {
  def toiletId = column[UUID]("toilet_id", O.SqlType("UUID"))
  def latitude = column[Double]("latitude")
  def longitude = column[Double]("longitude")
  def version = column[Int]("version")
  def updatedAt = column[java.time.LocalDateTime]("updated_at", O.Default(java.time.LocalDateTime.now()))

  override def * = (toiletId, latitude, longitude, version, updatedAt)
}
