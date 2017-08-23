package utils

import java.util.UUID


object GeneralUtils
{
  def getRandomUuid(): String = UUID.randomUUID().toString
}
