package services

import scala.concurrent.Future

trait CachingService
{
  def set(key: String, value: String): Future[Boolean]

  def get(key: String): Future[Option[String]]

  def remove(key: String): Future[Boolean]
}
