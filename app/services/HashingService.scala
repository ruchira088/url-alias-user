package services

import models.{Credentials, User}

import scala.concurrent.Future

trait HashingService
{
  def hash(password: String): Future[Credentials]

  def isMatch(password: String, user: User): Future[Boolean]
}
