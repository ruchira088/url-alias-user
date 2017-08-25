package controllers.responses

import play.api.libs.json.Json

case class LoginSuccess(token: String)

object LoginSuccess
{
  implicit val jsonFormat = Json.format[LoginSuccess]
}