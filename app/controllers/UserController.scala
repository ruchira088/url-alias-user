package controllers

import javax.inject.{Inject, Singleton}

import controllers.actions.AuthenticatedAction
import controllers.actions.authorization.{AuthorizedActionBuilder, Read_Permission}
import controllers.form.{CreateUserRequest, LoginUserRequest}
import controllers.requests.AuthenticatedRequest
import controllers.responses.LoginSuccess
import exceptions.InvalidCredentialsException
import play.api.libs.json.Json
import play.api.mvc._
import services._

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(controllerComponents: ControllerComponents,
                               authenticationService: AuthenticationService,
                               userService: UserService,
                               authenticatedAction: AuthenticatedAction,
                               authorizedActionBuilder: AuthorizedActionBuilder
                              )
                               (implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{

  def create(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] => for {
      createUserRequest <- Future.fromTry(CreateUserRequest.fromRequest)
      writeResult <- userService.create(createUserRequest) if writeResult.ok
    } yield {
      Ok(createUserRequest.toJson)
    }
  }

  def login(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] => {
      for {
        loginUserRequest <- Future.fromTry(LoginUserRequest.fromRequest)
        authenticationTokenValue <- authenticationService.authenticate(loginUserRequest)
      } yield Ok(Json.toJson(LoginSuccess(authenticationTokenValue.token)))
    } recover {
      case InvalidCredentialsException => Unauthorized(InvalidCredentialsException.toJson)
    }
  }

  def fetch(username: String): Action[AnyContent] = authenticatedAction
    .andThen(authorizedActionBuilder.create(username, Read_Permission)).async {
    implicit request: Request[AnyContent] => request match {
      case AuthenticatedRequest(authenticatedUser, _) =>
        if (authenticatedUser.username == username)
          Future.successful(Ok(authenticatedUser.toResponse))
        else userService.fetch(username).map(user => Ok(user.toResponse))
    }
  }
}

object UserController
{
  val USER_COLLECTION_NAME = "users"
}
