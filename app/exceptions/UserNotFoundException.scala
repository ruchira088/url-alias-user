package exceptions

case class UserNotFoundException(username: String) extends Exception
{
  override def getMessage = s"Unable to find user: ${username}"
}
