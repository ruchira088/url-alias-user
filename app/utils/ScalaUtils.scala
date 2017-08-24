package utils

import exceptions.EmptyOptionException

import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  def toTry[A](exceptionMessage: String = "Empty option")(option: Option[A]): Try[A] = option match {
    case Some(value) => Success(value)
    case None => Failure(EmptyOptionException(exceptionMessage))
  }
}
