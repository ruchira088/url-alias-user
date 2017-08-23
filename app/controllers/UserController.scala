package controllers

import javax.inject.{Inject, Singleton}

import controllers.form.CreateUserRequest
import models.User
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}
import services.{DatabaseService, HashingService}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject() (controllerComponents: ControllerComponents, databaseService: DatabaseService, hashingService: HashingService)
                               (implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents)
{
  def create() = Action.async {
    implicit request: Request[AnyContent] => for {
      createUserRequest <- Future.fromTry(CreateUserRequest.fromRequest)
      credentials <- hashingService.hash(createUserRequest.password, None)
      collection <- databaseService.getCollection("users")
      writeResult <- collection.insert(User(createUserRequest, credentials)) if writeResult.ok
    } yield {
      Ok(createUserRequest.toJson)
    }
  }

}
