package exceptions

import play.api.libs.json.{JsObject, Json}

object InvalidCredentialsException extends Exception
{
  def toJson: JsObject = Json.obj("result" -> "Invalid credentials")
}
