package controllers.actions.authorization

import javax.inject.Inject

import controllers.requests.AuthenticatedRequest
import exceptions.PermissionDeniedException
import play.api.mvc._
import services.AuthorizationService
import utils.ScalaUtils

import scala.concurrent.{ExecutionContext, Future}

class AuthorizedActionBuilder @Inject()(parser: BodyParsers.Default, authorizationService: AuthorizationService)
                                       (implicit executionContext: ExecutionContext)
{
  def create(username: String, permission: Permission): ActionBuilder[Request, AnyContent] = new ActionBuilderImpl(parser)
  {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] =
      request match {
        case AuthenticatedRequest(authenticatedUser, _) => for {
          isAuthorized <- authorizationService.isAuthorized(authenticatedUser, username)
          _ <- ScalaUtils.predicate(isAuthorized, PermissionDeniedException)
          result <- block(request)
        } yield result
      }
  }
}
