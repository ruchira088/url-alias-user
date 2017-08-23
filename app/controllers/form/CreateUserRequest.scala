package controllers.form

import exceptions.FormValidationException
import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{JsValue, Json, OFormat}
import play.api.mvc.{AnyContent, Request}

import scala.util.{Failure, Success, Try}

case class CreateUserRequest(email: String, username: String, password: String)
{
  request =>

  def toJson: JsValue = Json.toJson(request.copy(password = "*********"))
}

object CreateUserRequest
{
  implicit val jsonFormat: OFormat[CreateUserRequest] = Json.format[CreateUserRequest]

  def fromRequest(implicit request: Request[AnyContent]): Try[CreateUserRequest] =
  {
    Form(
      mapping(
        "username" -> text,
        "email" -> email,
        "password" -> text
      )(CreateUserRequest.apply)(CreateUserRequest.unapply)
    )
      .bindFromRequest()
      .fold(
        form => Failure(FormValidationException(form.errors.toList)),
        Success(_)
      )
  }
}