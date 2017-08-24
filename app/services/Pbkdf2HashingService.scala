package services

import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.inject.Inject

import models.{Credentials, User}
import utils.GeneralUtils

import scala.concurrent.{ExecutionContext, Future}

class Pbkdf2HashingService @Inject()(implicit executionContext: ExecutionContext) extends HashingService
{
  private def hash(password: String, salt: Option[String] = None): Future[Credentials] =
    Future {

      val keyFactory = SecretKeyFactory.getInstance(Pbkdf2HashingService.DEFAULT_KEY_FACTORY)

      val saltKey = salt.getOrElse(GeneralUtils.getRandomUuid())

      val keySpec = new PBEKeySpec(
        password.toCharArray, saltKey.getBytes, Pbkdf2HashingService.HASHING_ITERATIONS, Pbkdf2HashingService.KEY_LENGTH
      )

      val passwordHash = keyFactory.generateSecret(keySpec).getEncoded
        .map(byte => Math.abs(byte.toInt).toChar)
        .filter(_.isLetterOrDigit)
        .mkString

      Credentials(passwordHash, saltKey)
    }

  override def hash(password: String): Future[Credentials] = hash(password, None)

  override def isMatch(password: String, user: User): Future[Boolean] = for {
    credentials <- hash(password, Some(user.credentials.salt))
  } yield credentials.passwordHash == user.credentials.passwordHash
}

object Pbkdf2HashingService
{
  val KEY_LENGTH = 1024

  val HASHING_ITERATIONS = 1000

  val DEFAULT_KEY_FACTORY = "PBKDF2WithHmacSHA512"
}