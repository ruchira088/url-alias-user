package services

import javax.inject.Inject

import akka.actor.ActorSystem
import com.google.inject.Singleton
import play.api.inject.ApplicationLifecycle
import redis.RedisClient

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class RedisCachingService @Inject() (applicationLifecycle: ApplicationLifecycle)
                                    (implicit executionContext: ExecutionContext, actorSystem: ActorSystem)
  extends CachingService
{
  val redisClient: RedisClient = RedisClient(host = getRedisHost, port = getRedisHostPort)

  def set(key: String, value: String): Future[Boolean] = redisClient.set(key, value)

  def get(key: String): Future[Option[String]] = redisClient.get(key).map(_.map(_.utf8String))

  def remove(key: String): Future[Boolean] = redisClient.del(key).map(_ > 0)

  def getRedisHost: String = "localhost"

  def getRedisHostPort: Int = 6379

  applicationLifecycle.addStopHook {
    () => {
      println("Shutting down Redis client")
      redisClient.quit()
    }
  }

}
