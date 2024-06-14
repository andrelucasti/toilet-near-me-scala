package io.andrelucas
package infrastructure.db.toilet

import application.toilet.{Toilet, ToiletId, ToiletRepository}

import scala.concurrent.Future

class ToiletInMemoryRepository extends ToiletRepository {
  private val buffer = scala.collection.mutable.Buffer.empty[Toilet]
  
  override def save(toilet: Toilet): Future[Unit] = {
    buffer += toilet
    println(s"Saving toilet: $toilet")
    Future.successful(())
  }
  
  override def findById(id: ToiletId): Future[Option[Toilet]] = {
    println(s"Finding toilet by id: $id")
    Future.successful(buffer.find(_.id == id))
  }
  
  override def findAll(): Future[Seq[Toilet]] = {
    println(s"Finding all toilets")
    Future.successful(buffer.toSeq)
  }
  
  override def delete(id: ToiletId): Future[Unit] = {
    println(s"Deleting toilet by id: $id")
    buffer --= buffer.filter(_.id == id)
    Future.successful(())
  }
  
  override def update(toilet: Toilet): Future[Unit] = {
    println(s"Updating toilet: $toilet")
    buffer --= buffer.filter(_.id == toilet.id)
    buffer += toilet
    Future.successful(())
  }
}
