package io.andrelucas
package application.customer

case class CustomerNotFoundException(msg:String) extends RuntimeException(msg, null, true, false) 
