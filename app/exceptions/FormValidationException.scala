package exceptions

import play.api.data.FormError
import play.api.libs.json.Json
import play.api.mvc.Results

case class FormValidationException(formErrors: List[FormError]) extends Exception with ErrorResponse
{
  override def status = Results.UnprocessableEntity

  override def toResponse = {
    val validationErrors = formErrors.map(formError =>
      Json.obj("fieldName" -> formError.key, "errorMessage" -> formError.message)
    )
    status(Json.obj("validationErrors" -> validationErrors))
  }
}
