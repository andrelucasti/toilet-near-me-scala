package io.andrelucas
package application.toilet

import io.andrelucas.application.commons.NameInvalidException

import scala.util.Try

case class Toilet(id: ToiletId,
                  name: String,
                  geolocation: Geolocation,
                  toiletType: ToiletType,
                  price: Long)

case object Toilet:
  def create(name: String,
             latitude: Double,
             longitude: Double,
             price: Long = 0): Try[Toilet] = {
    Try{
      if (name.isEmpty) throw NameInvalidException("Description cannot be empty")
      if (price > 0) then
        createPaidToilet(name, latitude, longitude, price)
      else
        createFreeToilet(name, latitude, longitude)
    }
  }

  private def createFreeToilet(name: String, latitude: Double, longitude: Double): Toilet = {
    Toilet(ToiletId.generate, name, Geolocation(latitude, longitude), ToiletType.Free, 0)
  }

  private def createPaidToilet(name: String, latitude: Double, longitude: Double, price: Long): Toilet = {
    Toilet(ToiletId.generate, name, Geolocation(latitude, longitude), ToiletType.Paid, price)
  }

enum ToiletType:
  case Free, Paid

