package models

import controllers.form.CreateUserRequest
import reactivemongo.bson.{BSONDocumentHandler, Macros}
import utils.GeneralUtils

case class User(id: String, username: String, credentials: Credentials, email: String)

object User
{
  implicit val bsonDocumentHandler: BSONDocumentHandler[User] = Macros.handler[User]

  def apply(createUserRequest: CreateUserRequest, credentials: Credentials): User =
  {
    val CreateUserRequest(email, username, _) = createUserRequest

    User(GeneralUtils.getRandomUuid(), username, credentials, email)
  }
}