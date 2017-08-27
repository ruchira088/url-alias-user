package services

import javax.inject.{Inject, Singleton}

import controllers.form.LoginUserRequest
import exceptions.InvalidCredentialsException
import play.api.libs.json.{JsObject, Json}
import utils.{FutureO, GeneralUtils, ScalaUtils}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthenticationService @Inject()(
                                       cachingService: CachingService,
                                       hashingService: HashingService,
                                       userService: UserService
                                     )(implicit executionContext: ExecutionContext)
{
  def addToken(token: String, authenticationTokenValue: AuthenticationTokenValue): Future[Boolean] =
    cachingService.set(token, authenticationTokenValue.toJson.toString)

  def getUserFromToken(token: String): FutureO[AuthenticationTokenValue] =
    FutureO {
      for {
        value <- cachingService.get(token)
      } yield {
        value
          .map(Json.parse)
          .flatMap(jsValue => AuthenticationTokenValue.fromJson(jsValue.as[JsObject]))
      }
    }

  def removeToken(token: String): Future[Boolean] = cachingService.remove(token)

  def authenticate(loginUserRequest: LoginUserRequest): Future[AuthenticationTokenValue] = for {
    user <- userService.fetch(loginUserRequest.username)
    isMatch <- hashingService.isMatch(loginUserRequest.password, user)
    _ <- ScalaUtils.predicate(isMatch, InvalidCredentialsException)
    authenticationTokenValue = AuthenticationTokenValue(GeneralUtils.getRandomUuid(), user.sanitize)
    _ <- addToken(authenticationTokenValue.token, authenticationTokenValue)
  } yield authenticationTokenValue
}