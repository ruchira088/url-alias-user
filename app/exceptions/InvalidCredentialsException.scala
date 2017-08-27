package exceptions

import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Results

object InvalidCredentialsException extends Exception with ErrorResponse
{
  override def getMessage = "Invalid credentials"

  override def status = Results.Unauthorized
}
