package exceptions

import play.api.mvc.Results

case class UserNotFoundException(username: String) extends Exception with ErrorResponse
{
  override def getMessage = s"Unable to find user: $username"

  override def status: Results.Status = Results.NotFound
}
