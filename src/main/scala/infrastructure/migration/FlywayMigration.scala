package io.andrelucas
package infrastructure.migration

import org.flywaydb.core.Flyway

import scala.util.Properties

class FlywayMigration extends Migration {

  override def migrate(): Unit =
    
    val url = Properties.envOrElse("DATABASE_URL", "jdbc:postgresql://localhost:5432/toilet")
    val user = Properties.envOrElse("DATABASE_USER", "toilet")
    val password = Properties.envOrElse("DATABASE_PASSWORD", "toilet")
    
    val flyway =
      Flyway.configure()
        .dataSource(url, user, password)
        .locations("classpath:db/migration")
        .load()

    flyway.migrate()
}
