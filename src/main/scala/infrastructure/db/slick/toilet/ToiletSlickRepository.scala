package io.andrelucas
package infrastructure.db.slick.toilet

import application.toilet.{Toilet, ToiletId, ToiletRepository}

import slick.jdbc.JdbcBackend.Database

import scala.concurrent.{ExecutionContext, Future}
import slick.jdbc.PostgresProfile.api.*

class ToiletSlickRepository(db: Database) (implicit ec: ExecutionContext) extends ToiletRepository {
  private val toilets = TableQuery[ToiletTable]
  private val geolocation = TableQuery[ToiletGeolocationTable]
  
  override def save(entity: Toilet): Future[Unit] =
    val toiletVersion = 0
    val geolocationVersion = 0
    
    val action = for {
      _ <- toilets += (entity.id.value, entity.name, entity.toiletType.toString, entity.price, toiletVersion, java.time.LocalDateTime.now())
      _ <- geolocation += (entity.id.value, entity.geolocation.latitude, entity.geolocation.longitude, geolocationVersion, java.time.LocalDateTime.now())
    } yield ()
    
    db.run(action.transactionally).map(_ => ())
    
  override def findById(id: ToiletId): Future[Option[Toilet]] = ???
  override def findAll(): Future[Seq[Toilet]] = ???
  override def update(entity: Toilet): Future[Unit] = ???
  override def delete(id: ToiletId): Future[Unit] = ???
}
