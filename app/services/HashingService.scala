package services

import models.Credentials

import scala.concurrent.Future

trait HashingService
{
  def hash(password: String, salt: Option[String]): Future[Credentials]
}
