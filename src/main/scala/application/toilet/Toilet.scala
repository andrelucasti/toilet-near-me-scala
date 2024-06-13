package io.andrelucas
package application.toilet

import io.andrelucas.application.commons.NameInvalidException

import scala.util.Try

case class Toilet(name: String, 
                  geolocation: Geolocation,
                  toiletType: ToiletType,
                  price: Long)

case object Toilet:
  def create(name: String,
             latitude: Double,
             longitude: Double,
             toiletType: ToiletType,
             price: Long = 0): Try[Toilet] = {
    Try{
      if (name.isEmpty) throw NameInvalidException("Description cannot be empty")
      if (toiletType.equals(ToiletType.Paid) && price <= 0) throw ToiletInvalidException("Toilet paid must have a price")
      Toilet(name, Geolocation(latitude, longitude), toiletType, price)
    }
  }
  
enum ToiletType:
  case Free, Paid

