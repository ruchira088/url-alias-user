package exceptions

import play.api.mvc.Results

object IncorrectAuthorizationTokenException extends Exception with ErrorResponse
{
  override def getMessage = "Incorrect authorization token"

  override def status = Results.Unauthorized
}