package services

import reactivemongo.api.collections.bson.BSONCollection

import scala.concurrent.Future

trait DatabaseService
{
  def getCollection(collectionName: String): Future[BSONCollection]

}
