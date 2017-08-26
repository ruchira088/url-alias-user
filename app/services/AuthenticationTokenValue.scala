package services

import models.User
import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json}

import scala.concurrent.duration._

case class AuthenticationTokenValue(token: String, user: User, expiryDate: DateTime)
{
  def toJson: JsObject = Json.obj("token" -> token, "user" -> Json.toJson(user), "expiryDate" -> expiryDate.toString)
}

object AuthenticationTokenValue
{
  val TOKEN_DURATION: Duration = 5 minutes

  def apply(token: String, user: User): AuthenticationTokenValue
    = AuthenticationTokenValue(token, user, DateTime.now().plus(TOKEN_DURATION.toMillis))

  def fromJson(jsObject: JsObject): Option[AuthenticationTokenValue] = for {
    token <- (jsObject \ "token").toOption
    user <- (jsObject \ "user").toOption.map(_.as[User])
    expiryDate <- (jsObject \ "expiryDate").toOption.map(jsValue => DateTime.parse(jsValue.as[String]))
  } yield AuthenticationTokenValue(token.toString, user, expiryDate)

}