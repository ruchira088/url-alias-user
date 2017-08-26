package models

import controllers.form.CreateUserRequest
import play.api.libs.json.{Json, OFormat}
import reactivemongo.bson.{BSONDocumentHandler, Macros}
import utils.GeneralUtils

case class User(id: String, username: String, credentials: Credentials, email: String) extends ResponseModel[User]
{
  user =>

  def sanitize: User = user.copy(credentials = Credentials.empty)

  override def toResponse = user.sanitize
}

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