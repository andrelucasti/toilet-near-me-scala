package io.andrelucas
package infrastructure.migration

trait Migration {
  def migrate(): Unit
}
