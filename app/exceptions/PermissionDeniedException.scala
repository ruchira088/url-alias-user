package exceptions

import play.api.mvc.Results

object PermissionDeniedException extends Exception with ErrorResponse
{
  override def getMessage = "Permission denied"

  override def status = Results.Forbidden
}
