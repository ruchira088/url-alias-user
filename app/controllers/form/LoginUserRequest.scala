package controllers.form

import exceptions.FormValidationException
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.{AnyContent, Request}

import scala.util.{Failure, Success, Try}

case class LoginUserRequest(username: String, password: String)

object LoginUserRequest
{
  def fromRequest(implicit request: Request[AnyContent]): Try[LoginUserRequest] =
  {
    Form(
      mapping(
        "username" -> text,
        "password" -> text
      )(LoginUserRequest.apply)(LoginUserRequest.unapply)
    )
      .bindFromRequest()
      .fold(
        form => Failure(FormValidationException(form.errors.toList)),
        Success(_)
      )
  }
}