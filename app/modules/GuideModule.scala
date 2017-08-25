package modules

import com.google.inject.AbstractModule
import services._

class GuideModule extends AbstractModule
{
  override def configure() = {
    bind(classOf[DatabaseService]).to(classOf[MongoDatabase])
    bind(classOf[HashingService]).to(classOf[Pbkdf2HashingService])
    bind(classOf[CachingService]).to(classOf[RedisCachingService])
  }
}
