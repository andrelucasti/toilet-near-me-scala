package io.andrelucas
package application.toilet

case class ToiletInvalidException(msg:String) extends RuntimeException(msg, null, true, false) 
