package routes

import akka.actor.typed.{ActorRef, ActorSystem}
import akka.actor.typed.scaladsl.AskPattern._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.util.Timeout
import registry.ResumeRegistry._
import _root_.registry.{Resume, ResumeRegistry, Resumes}

import scala.concurrent.Future

class ResumeRoutes(resumeRegistry: ActorRef[ResumeRegistry.Command])(
  implicit val system: ActorSystem[_]
) {
  //#resume-routes-class
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import app.JsonFormats._
  //#import-json-formats

  // If ask takes more time than this to complete the request is failed
  private implicit val timeout = Timeout.create(
    system.settings.config.getDuration("my-app.routes.ask-timeout")
  )

  def getResumes(): Future[Resumes] = {
    resumeRegistry.ask(GetResumes)
  }
  def getResume(id: Int): Future[GetResumeResponse] = {
    resumeRegistry.ask(GetResume(id, _))
  }
  def createResume(resume: Resume): Future[ActionPerformed] = {
    resumeRegistry.ask(CreateResume(resume, _))
  }
  def deleteResume(id: Int): Future[ActionPerformed] = {
    resumeRegistry.ask((DeleteResume(id, _)))
  }
  //#all-routes
  //#resumes-get-post
  //#resumes-get-delete
  val resumeRoutes: Route =
    pathPrefix("resumes") {
      concat(
        //#resumes-get-delete
        pathEnd {
          concat(get {
            complete(getResumes())
          }, post {
            entity(as[Resume]) { resume =>
              onSuccess(createResume(resume)) { performed =>
                complete((StatusCodes.Created))
              }
            }
          })
        },
        //#resumes-get-delete
        //#resumes-get-post
        path(Segment) { id =>
          concat(get {
            //#retrieve-resume-info
            rejectEmptyResponse {
              onSuccess(getResume(id.toInt)) { response =>
                complete(response.maybeResume)
              }
            }
            //#retrieve-resume-info
          }, delete {
            //#resumes-delete-logic
            onSuccess(deleteResume(id.toInt)) { performed =>
              complete((StatusCodes.OK))
            }
            //#resumes-delete-logic
          })
        }
      )
      //#resumes-get-delete
    }
  //#all-routes

}
