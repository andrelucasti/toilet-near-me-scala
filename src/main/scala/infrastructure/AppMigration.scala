package io.andrelucas
package infrastructure

import infrastructure.migration.{FlywayMigration, Migration}

case class AppMigration(migration: Migration){
  def run(): Unit = migration.migrate()
}

object AppMigration {

  def main(args: Array[String]): Unit = {
    AppMigration(new FlywayMigration)
      .run()
  }
}
