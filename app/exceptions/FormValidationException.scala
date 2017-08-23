package exceptions

import play.api.data.FormError

case class FormValidationException(formErrors: List[FormError]) extends Exception
