package controllers

import javax.inject.{Inject, Singleton}

import controllers.form.{CreateUserRequest, LoginUserRequest}
import controllers.responses.LoginSuccess
import exceptions.{InvalidCredentialsException, UserNotFoundException}
import models.User
import play.api.libs.json.Json
import play.api.mvc._
import reactivemongo.api.Cursor
import reactivemongo.bson.document
import services.{AuthenticationTokenValue, AuthenticationTokens, DatabaseService, HashingService}
import utils.{GeneralUtils, ScalaUtils}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(controllerComponents: ControllerComponents,
                               databaseService: DatabaseService,
                               hashingService: HashingService,
                               authenticationTokens: AuthenticationTokens
                              )
                               (implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  private def fetchUser(username: String): Future[User] = for {
    userCollection <- databaseService.getCollection(UserController.USER_COLLECTION_NAME)
    users <- userCollection.find(document("username" -> username)).cursor[User]().collect[List](1, Cursor.FailOnError[List[User]]())
    user <- users match {
      case Nil => Future.failed(UserNotFoundException(username))
      case x :: _ => Future.successful(x)
    }
  } yield user

  def create(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] => for {
      createUserRequest <- Future.fromTry(CreateUserRequest.fromRequest)
      credentials <- hashingService.hash(createUserRequest.password)
      collection <- databaseService.getCollection(UserController.USER_COLLECTION_NAME)
      writeResult <- collection.insert(User(createUserRequest, credentials)) if writeResult.ok
    } yield {
      Ok(createUserRequest.toJson)
    }
  }

  def login(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] => {
      for {
        loginUserRequest <- Future.fromTry(LoginUserRequest.fromRequest)
        user <- fetchUser(loginUserRequest.username)
        isMatch <- hashingService.isMatch(loginUserRequest.password, user)
        _ <- if (isMatch) Future.successful(user) else Future.failed(InvalidCredentialsException)
        token = GeneralUtils.getRandomUuid()
        _ <- authenticationTokens.add(token, AuthenticationTokenValue(token, user.sanitize))
      } yield Ok(Json.toJson(LoginSuccess(token)))
    } recover {
      case InvalidCredentialsException => Unauthorized(InvalidCredentialsException.toJson)
    }
  }
}

object UserController
{
  val USER_COLLECTION_NAME = "users"
}
