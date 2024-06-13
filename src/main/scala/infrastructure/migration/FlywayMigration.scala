package io.andrelucas
package infrastructure.migration

import org.flywaydb.core.Flyway

class FlywayMigration extends Migration {

  override def migrate(): Unit =
    val flyway =
      Flyway.configure()
        .dataSource("jdbc:postgresql://localhost:5432/toilet", "toilet", "toilet")
        .locations("classpath:db/migration")
        .load()

    flyway.migrate()
}
