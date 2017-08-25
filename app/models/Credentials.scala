package models

import play.api.libs.json.{Json, OFormat}
import reactivemongo.bson.{BSONDocumentHandler, Macros}

case class Credentials(passwordHash: String, salt: String)

object Credentials
{
  def empty: Credentials = Credentials("", "")

  implicit val jsonFormat: OFormat[Credentials] = Json.format[Credentials]

  implicit val bsonDocumentHandler: BSONDocumentHandler[Credentials] = Macros.handler[Credentials]
}

