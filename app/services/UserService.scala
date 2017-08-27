package services

import javax.inject.{Inject, Singleton}

import controllers.form.CreateUserRequest
import exceptions.UserNotFoundException
import models.User
import reactivemongo.api.Cursor
import reactivemongo.api.commands.WriteResult
import reactivemongo.bson.document

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject()(
                             databaseService: DatabaseService,
                             hashingService: HashingService
                           )(implicit executionContext: ExecutionContext)
{
  def fetch(username: String): Future[User] = for {
    userCollection <- databaseService.getCollection(UserService.COLLECTION_NAME)
    users <- userCollection.find(document("username" -> username)).cursor[User]().collect[List](1, Cursor.FailOnError[List[User]]())
    user <- users match {
      case Nil => Future.failed(UserNotFoundException(username))
      case x :: _ => Future.successful(x)
    }
  } yield user

  def create(createUserRequest: CreateUserRequest): Future[WriteResult] = for {
    credentials <- hashingService.hash(createUserRequest.password)
    collection <- databaseService.getCollection(UserService.COLLECTION_NAME)
    writeResult <- collection.insert(User(createUserRequest, credentials))
  } yield writeResult
}

object UserService
{
  val COLLECTION_NAME = "users"
}
