package exceptions

import play.api.mvc.Results

object InvalidAuthorizationTokenException extends Exception with ErrorResponse
{
  override def getMessage = "Invalid authorization token. Accepted format: Bearer <tokenValue>"

  override def status = Results.Unauthorized
}
