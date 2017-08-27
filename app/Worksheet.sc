import org.joda.time.DateTime

val dateTime = DateTime.now()

DateTime.parse(dateTime.toString)

import controllers.form.CreateUserRequest
import org.joda.time.DateTime
import play.api.libs.json.{JsObject, Json, OFormat}
import reactivemongo.bson.{BSONDocumentHandler, Macros}
import utils.GeneralUtils

case class User(id: String, username: String, credentials: Credentials, email: String)

object User
{
  implicit val jsonFormat: OFormat[User] = Json.format[User]

  implicit val bsonDocumentHandler: BSONDocumentHandler[User] = Macros.handler[User]

  def apply(createUserRequest: CreateUserRequest, credentials: Credentials): User =
  {
    val CreateUserRequest(email, username, _) = createUserRequest

    User(GeneralUtils.getRandomUuid(), username, credentials, email)
  }
}

case class Credentials(passwordHash: String, salt: String)

object Credentials
{
  implicit val jsonFormat: OFormat[Credentials] = Json.format[Credentials]

  implicit val bsonDocumentHandler: BSONDocumentHandler[Credentials] = Macros.handler[Credentials]
}

case class AuthenticationTokenValue(token: String, user: User, expiryDate: DateTime)
{
  def toJson: JsObject = Json.obj("token" -> token, "user" -> Json.toJson(user), "expiryDate" -> expiryDate.toString)
}

object AuthenticationTokenValue
{
  def fromJson(jsObject: JsObject): Option[AuthenticationTokenValue] = for {
    token <- (jsObject \ "token").toOption
    user <- (jsObject \ "user").toOption.map(_.as[User])
    expiryDate <- (jsObject \ "expiryDate").toOption.map(jsValue => DateTime.parse(jsValue.as[String]))
  } yield AuthenticationTokenValue(token.toString, user, expiryDate)
}

val credentials = Credentials("password", "my")
val user = User("id", "john", credentials, "email")

val tokenValue = AuthenticationTokenValue("token", user, DateTime.now())

val json = tokenValue.toJson

AuthenticationTokenValue.fromJson(tokenValue.toJson)