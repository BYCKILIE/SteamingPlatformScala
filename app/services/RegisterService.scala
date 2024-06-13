package services

import javax.inject.Inject
import scala.concurrent.ExecutionContext
import scala.concurrent.Future
import org.mongodb.scala.bson.BsonObjectId
import org.mongodb.scala.model.Filters.{and, equal}
import org.mongodb.scala.model.Updates.{combine, set}
import org.mongodb.scala.{Document, MongoCollection}
import utils.Mongo
import DTO.RegisterDTO
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala._
import org.mongodb.scala.model.Indexes.ascending
import java.util.Date

class RegisterService @Inject() (implicit ex: ExecutionContext) {

  private val collection: MongoCollection[Document] = Mongo.database.getCollection("temp")

  private val indexOptions = IndexOptions().expireAfter(1800, java.util.concurrent.TimeUnit.SECONDS)
  collection.createIndex(ascending("createdAt"), indexOptions).subscribe(new Observer[String] {
    override def onNext(result: String): Unit = println(s"Index created: $result")
    override def onError(e: Throwable): Unit = println(s"Failed to create index: ${e.getMessage}")
    override def onComplete(): Unit = println("Completed")
  })

  def createTemp(email: String): Future[String] = {
    val document = Document(
      "_id"        -> BsonObjectId(),
      "email"      -> email,
      "first_name" -> "not_set",
      "last_name"  -> "not_set",
      "birth_date" -> "not_set",
      "gender"     -> 0,
      "createdAt" -> new Date()
    )
    collection.insertOne(document).toFuture().map { result =>
      if (result.wasAcknowledged()) {
        document.getObjectId("_id").toString
      } else {
        "error"
      }
    }
  }

  def readTemp(id: String): Future[RegisterDTO] = {
    collection.find(equal("_id", BsonObjectId(id))).headOption().flatMap { maybeDocument =>
      maybeDocument
        .map { document =>
          val email     = document.getString("email")
          val firstName = document.getString("first_name")
          val lastName  = document.getString("last_name")
          val birthDate = document.getString("birth_date")
          val gender    = document.getInteger("gender")

          Future.successful(RegisterDTO(email, firstName, lastName, birthDate, gender))
        }
        .getOrElse(Future.failed(new NoSuchElementException(s"Not found")))
    }
  }

  def completeTemp(id: String, firstName: String, lastName: String, birthDate: String, gender: Int): Future[Boolean] = {
    val filter = and(
      equal("_id", BsonObjectId(id))
    )

    val update = combine(
      set("first_name", firstName),
      set("last_name", lastName),
      set("birth_date", birthDate),
      set("gender", gender)
    )

    collection.updateOne(filter, update).toFuture().map { result =>
      result.wasAcknowledged() && result.getModifiedCount > 0
    }
  }

  def deleteTemp(id: String): Future[Boolean] = {
    val filter = and(
      equal("_id", BsonObjectId(id))
    )
    collection.deleteOne(filter).toFuture().map { result =>
      result.wasAcknowledged() && result.getDeletedCount > 0
    }
  }

}
