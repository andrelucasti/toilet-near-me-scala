package io.andrelucas
package application.toilet

import java.util.UUID

case class ToiletId(value: UUID):
  def asString: String = value.toString
  
case object ToiletId extends IdGenerator:
  def apply(value: UUID): ToiletId = new ToiletId(value)
  def generate: ToiletId = ToiletId(generator)
