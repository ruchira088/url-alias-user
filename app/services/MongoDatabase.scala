package services

import javax.inject.{Inject, Singleton}

import play.api.inject.ApplicationLifecycle
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{DefaultDB, MongoConnection, MongoDriver}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

@Singleton
class MongoDatabase @Inject()(applicationLifecycle: ApplicationLifecycle)
                             (implicit executionContext: ExecutionContext) extends DatabaseService
{
  val mongoDriver = MongoDriver()

  val connection: Try[MongoConnection] =
    MongoConnection.parseURI(getMongoDbUri)
      .map(mongoDriver.connection)

  def getDb(databaseName: String): Future[DefaultDB] = for {
    dbConnection <- Future.fromTry(connection)
    database <- dbConnection.database(databaseName)
  } yield database

  def getCollection(collectionName: String): Future[BSONCollection] = for {
    database <- getDb(getDefaultDatabaseName)
  } yield database.collection[BSONCollection](collectionName)

  def getMongoDbUri: String = "mongodb://localhost:27017"

  def getDefaultDatabaseName: String = "url_alias_user"

  applicationLifecycle.addStopHook {
    () => {
      println("Closing mongoDriver...")
      Future.successful(mongoDriver.close())
    }
  }
}
