package controllers.actions

import javax.inject.Inject

import constants.HttpHeaders
import controllers.requests.AuthenticatedRequest
import exceptions.{ErrorResponse, IncorrectAuthorizationTokenException, InvalidAuthorizationTokenException}
import play.api.mvc._
import services.AuthenticationService
import utils.FutureO

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class AuthenticatedAction @Inject()(
                                     parser: BodyParsers.Default,
                                     authenticationService: AuthenticationService
                                   ) (implicit executionContext: ExecutionContext)
  extends ActionBuilderImpl(parser)
{
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] =
  {
    val futureO: FutureO[Result] = for {
      authorizationHeader <- FutureO.fromOption(request.headers.get(HttpHeaders.AUTHORIZATION))
      tokenString <- FutureO.fromTry(AuthenticatedAction.getTokenFromHeader(authorizationHeader))
      authenticationToken <- authenticationService.getUserFromToken(tokenString)
      result <- FutureO.fromFuture(block(AuthenticatedRequest(authenticationToken.user, request)))
    } yield result

    futureO.future.flatMap {
      case Some(result) => Future.successful(result)
      case None => Future.failed(IncorrectAuthorizationTokenException)
    } recover {
      case errorResponse: ErrorResponse => errorResponse.toResponse
    }
  }
}

object AuthenticatedAction
{
  def getTokenFromHeader(authorizationHeader: String): Try[String] = authorizationHeader.trim.split(" ").toList match {
    case List("Bearer", token) => Success(token)
    case _ => Failure(InvalidAuthorizationTokenException)
  }
}