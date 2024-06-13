package io.andrelucas
package application.toilet

case class GeolocationException(msg:String) extends RuntimeException(msg, null, true, false) {

}
