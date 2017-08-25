package services

import javax.inject.{Inject, Singleton}

import play.api.libs.json.{JsObject, Json}

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthenticationTokens @Inject()(cachingService: CachingService) (implicit executionContext: ExecutionContext)
{
  def add(token: String, authenticationTokenValue: AuthenticationTokenValue): Future[Boolean] =
    cachingService.set(token, authenticationTokenValue.toJson.toString)

  def get(token: String): Future[Option[AuthenticationTokenValue]] = for {
    value <- cachingService.get(token)
  } yield {
    value
      .map(Json.parse)
      .flatMap(jsValue => AuthenticationTokenValue.fromJson(jsValue.as[JsObject]))
  }

  def remove(token: String): Future[Boolean] = cachingService.remove(token)
}
