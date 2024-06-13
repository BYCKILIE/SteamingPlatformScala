package utils

import org.mongodb.scala.{MongoClient, MongoDatabase}

object Mongo {
  private val client: MongoClient = MongoClient("mongodb://localhost:27017")
  val database: MongoDatabase = client.getDatabase("StreamingPlatform")
}
