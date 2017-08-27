package services

import javax.inject.{Inject, Singleton}

import models.User

import scala.concurrent.Future

@Singleton
class AuthorizationService @Inject()()
{
  def isAuthorized(authenticatedUser: User, clientUsername: String): Future[Boolean] =
    Future.successful(authenticatedUser.username == clientUsername)
}
