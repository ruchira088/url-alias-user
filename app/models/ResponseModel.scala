package models

import play.api.libs.json.JsValue

trait ResponseModel
{
  def toResponse: JsValue
}
