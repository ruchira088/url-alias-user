package controllers.requests

import models.User
import play.api.mvc.{Request, WrappedRequest}

case class AuthenticatedRequest[A](authenticatedUser: User, request: Request[A]) extends WrappedRequest[A](request)
