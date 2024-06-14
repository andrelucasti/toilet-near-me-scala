package io.andrelucas
package application.toilet

import java.util.UUID

case class ToiletId(id: UUID):
  def asString: String = id.toString
  
case object ToiletId extends IdGenerator:
  def apply(value: UUID): ToiletId = new ToiletId(value)
  def generate: ToiletId = ToiletId(generator)
