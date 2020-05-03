package models

import io.circe.syntax._
import io.circe._

case class Resume(id: Int,
                  name: String,
                  surname: String,
                  education: Education,
                  experience: Experience,
                  language: Language,
                  certification: Certification,
                  project: Project,
                  hobby: List[String],
                  computerSkills: List[String]) {
  require(name != null, "name not filled")
  require(name != null, "surname not filled")

}
case class Education(degree: String, university: String, date: String)
case class Experience(company: String,
                      position: String,
                      location: String,
                      description: String,
                      date: String)
case class Language(language: String, level: String)
case class Certification(name: String, institution: String, date: String)
case class Project(projectName: String, description: String)
object Resume {}
object Project {
  implicit val encoder: Encoder[Project] = (p: Project) => {
    Json.obj(
      "projectName" -> p.projectName.asJson,
      "description" -> p.description.asJson,
    )
  }
  implicit val decoder: Decoder[Project] = (c: HCursor) => {
    for {
      projectName <- c.downField("projectName").as[String]
      description <- c.downField("description").as[String]
    } yield Project(projectName, description)
  }
}
