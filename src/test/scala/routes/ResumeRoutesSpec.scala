package routes

//#resume-routes-spec
//#test-top

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.scaladsl.adapter._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{Matchers, WordSpec}
import registry.{Resume, ResumeRegistry}

//#set-up
class ResumeRoutesSpec
    extends WordSpec
    with Matchers
    with ScalaFutures
    with ScalatestRouteTest {
  //#test-top

  // the Akka HTTP route testkit does not yet support a typed actor system (https://github.com/akka/akka-http/issues/2036)
  // so we have to adapt for now
  lazy val testKit = ActorTestKit()
  implicit def typedSystem = testKit.system
  override def createActorSystem(): akka.actor.ActorSystem =
    testKit.system.toClassic

  // We use the real UserRegistryActor to test it while we hit the Routes,
  // but we could "mock" it by implementing it in-place or by using a TestProbe
  // created with testKit.createTestProbe()
  val resumeRegistry = testKit.spawn(ResumeRegistry())
  lazy val routes = new ResumeRoutes(resumeRegistry).resumeRoutes

  // use the json formats to marshal and unmarshall objects in the test
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import app.JsonFormats._
  //#set-up

  //#actual-test
  "ResumeRoutes" should {
    "return no resumes if no present (GET /users)" in {
      // note that there's no need for the host part in the uri:
      val request = HttpRequest(uri = "/resumes")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        contentType should ===(ContentTypes.`application/json`)
        // and no entries should be in the list:
        entityAs[String] should ===("""{"resumes":[]}""")
      }
    }
    //#actual-test

    //#testing-post
    "be able to add resumes (POST /resumes)" in {
      val resume =
        Resume(
          2,
          "John",
          "Doe",
          None,
          None,
          None,
          None,
          None,
          Some(List("a", "b")),
          Some(List("C", "Java", "Scala"))
        )
      val resumeEntity = Marshal(resume).to[MessageEntity].futureValue // futureValue is from ScalaFutures

      // using the RequestBuilding DSL:
      val request = Post("/resumes").withEntity(resumeEntity)

      request ~> routes ~> check {
        status should ===(StatusCodes.Created)

        // we expect the response to be json:
        //contentType should ===(ContentTypes.`application/json`)

        // and we know what message we're expecting back:
        entityAs[String] should ===("Resume for John created.")
      }
    }
    //#testing-post

    "be able to remove users (DELETE /resumes)" in {
      // resume the RequestBuilding DSL provided by ScalatestRouteSpec:
      val request = Delete(uri = "/resumes/2")

      request ~> routes ~> check {
        status should ===(StatusCodes.OK)

        // we expect the response to be json:
        //contentType should ===(ContentTypes.`application/json`)

        // and no entries should be in the list:
        entityAs[String] should ===("Resume for 2 deleted.")
      }
    }
    //#actual-test
  }
  //#actual-test

  //#set-up
}
//#set-up
//#resume-routes-spec
