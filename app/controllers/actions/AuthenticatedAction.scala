package controllers.actions

import javax.inject.Inject

import play.api.mvc.{ActionBuilderImpl, BodyParsers, Request, Result}

import scala.concurrent.{ExecutionContext, Future}

class AuthenticatedAction @Inject() (parser: BodyParsers.Default) (implicit executionContext: ExecutionContext)
  extends ActionBuilderImpl(parser)
{
  override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]): Future[Result] =
  {
    ???
  }
}
