package controllers.actions.authorization

sealed trait Permission

case object Modify_Permission extends Permission
case object Read_Permission extends Permission
