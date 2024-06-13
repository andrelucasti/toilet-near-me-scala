package io.andrelucas
package application.commons

case class NameInvalidException(msg: String) extends RuntimeException(msg, null, true, false)
