package io.andrelucas
package application.toilet

case class Geolocation(latitude: Double, longitude: Double)

case object Geolocation:
  
  def apply(latitude: Double, longitude: Double): Geolocation =
    geolocationValidation(latitude, longitude)
    
    new Geolocation(latitude, longitude)

  private def geolocationValidation(latitude: Double,
                            longitude: Double): Unit = {

    val checkLatitude = latitude < -90 || latitude > 90
    val checkLongitude = longitude < -180 || longitude > 180

    if (checkLatitude && checkLongitude) throw GeolocationException("Invalid geolocation")
    if (checkLatitude) throw GeolocationException("Invalid latitude")
    if (checkLongitude) throw GeolocationException("Invalid longitude")
  }

