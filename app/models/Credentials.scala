package models

import reactivemongo.bson.{BSONDocumentHandler, Macros}

case class Credentials(passwordHash: String, salt: String)

object Credentials
{
  implicit val bsonDocumentHandler: BSONDocumentHandler[Credentials] = Macros.handler[Credentials]
}

