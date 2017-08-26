package utils

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try

case class FutureO[A](future: Future[Option[A]])
{
  def flatMap[B](f: A => FutureO[B])(implicit executionContext: ExecutionContext): FutureO[B] =
    FutureO {
      future.flatMap {
        case Some(value) => f(value).future
        case None => Future.successful(None)
      }
  }

  def map[B](f: A => B)(implicit executionContext: ExecutionContext): FutureO[B] =
    FutureO { future.map(_ map f) }
}

object FutureO
{
  def fromFuture[A](future: Future[A])(implicit executionContext: ExecutionContext): FutureO[A] =
    FutureO {
      future.map(Some(_))
    }

  def fromTry[A](tryValue: Try[A])(implicit executionContext: ExecutionContext): FutureO[A] =
    fromFuture(Future.fromTry(tryValue))

  def fromOption[A](option: Option[A])(implicit executionContext: ExecutionContext): FutureO[A] =
    fromTry(ScalaUtils.toTry()(option))


}