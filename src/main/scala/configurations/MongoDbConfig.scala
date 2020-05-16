package configurations

import com.typesafe.config.ConfigFactory
import org.mongodb.scala.{Document, MongoClient, MongoCollection, MongoDatabase}
import org.bson.codecs.configuration.CodecRegistries._
import org.mongodb.scala._
import org.mongodb.scala.MongoClient.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import registry.{Certification, Education, Experience, Language, Resume}

object MongoDbConfig {
  lazy val config = ConfigFactory.load()
  lazy val mongoClient: MongoClient = MongoClient(config.getString("mongo.uri"))
  lazy val codecRegistry =
    fromRegistries(
      fromProviders(
        classOf[Resume],
        classOf[Education],
        classOf[Experience],
        classOf[Language],
        classOf[Certification],
        classOf[Language]
      ),
      DEFAULT_CODEC_REGISTRY
    )
  lazy val database: MongoDatabase = mongoClient
    .getDatabase(config.getString("mongo.database"))
    .withCodecRegistry(codecRegistry)

  //test
  database.createCollection("mydb")
  val collection: MongoCollection[Document] = database.getCollection("mydb")

  def addToCollection() = {
    val document: Document = Document("_id" -> 2, "x" -> 1)
    val insertObservable = collection.insertOne(document)

    insertObservable.subscribe(new Observer[Completed] {
      override def onNext(result: Completed): Unit = println(s"onNext: $result")
      override def onError(e: Throwable): Unit = println(s"onError: $e")
      override def onComplete(): Unit = println("onComplete")
    })
  }
  def getFromCollection() = {
    collection
      .find()
      .collect()
      .subscribe(
        (results: Seq[Document]) =>
          println(s"[${database.name}]Found Documents: #${results.size}")
      )
  }
}
