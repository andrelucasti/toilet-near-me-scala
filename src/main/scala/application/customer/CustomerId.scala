package io.andrelucas
package application.customer

import CustomerId.generator

import java.util.UUID

case class CustomerId(value: UUID):
  def asString: String = value.toString

case object CustomerId extends IdGenerator:
  def apply(value: UUID): CustomerId = new CustomerId(value)
  def generate: CustomerId = CustomerId(generator)
