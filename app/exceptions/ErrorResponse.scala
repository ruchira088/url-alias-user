package exceptions

import play.api.libs.json.Json
import play.api.mvc.{Result, Results}

trait ErrorResponse
{
  exception: Exception =>

  def status: Results.Status

  def toResponse: Result = status(Json.obj("error" -> exception.getMessage))
}
