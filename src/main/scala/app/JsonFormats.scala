package app

import registry.UserRegistry.ActionPerformed
import registry._
import spray.json.DefaultJsonProtocol

object JsonFormats {
  // import the default encoders for primitive types (Int, String, Lists etc)
  import DefaultJsonProtocol._

  implicit val userJsonFormat = jsonFormat3(User)
  implicit val usersJsonFormat = jsonFormat1(Users)

  implicit val educationJsonFormat = jsonFormat3(Education)
  implicit val experienceJsonFormat = jsonFormat5(Experience)
  implicit val languageJsonFormat = jsonFormat2(Language)
  implicit val projectJsonFormat = jsonFormat2(Project)
  implicit val certificationJsonFormat = jsonFormat3(Certification)
  implicit val resumeJsonFormat = jsonFormat10(Resume)
  implicit val resumesJsonFormat = jsonFormat1(Resumes)

  implicit val actionPerformedJsonFormat = jsonFormat1(ActionPerformed)
}
