package models

trait ResponseModel[A]
{
  def toResponse: A
}
