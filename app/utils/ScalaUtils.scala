package utils

import exceptions.EmptyOptionException

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

object ScalaUtils
{
  def toTry[A](exceptionMessage: String = "Empty option")(option: Option[A]): Try[A] = option match {
    case Some(value) => Success(value)
    case None => Failure(EmptyOptionException(exceptionMessage))
  }

  def predicate(condition: Boolean, exception: => Exception): Future[Unit] =
    if (condition) Future.successful(()) else Future.failed(exception)
}
