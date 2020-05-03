val versions = new {
  val scalaMongoDriver = "2.9.0"
  val javaMongoDriver = "3.12.3"
  val akkaHttpCirce = "1.32.0"
  val circe = "0.13.0"
  val akkaHttpVersion = "10.1.11"
  val akkaVersion = "2.6.5"
  val logback = "1.2.3"
  val scalaTest = "3.0.8"
  val fakeMongo = "2.1.1"
  val pegdown = "1.6.0"
}

lazy val root = (project in file(".")).settings(
  resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/",
  resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
  inThisBuild(List(organization := "com.baboci", scalaVersion := "2.13.1")),
  name := "Resume",
  libraryDependencies ++= Seq(
    //mongo
    "org.mongodb.scala" %% "mongo-scala-driver" % versions.scalaMongoDriver,
    "org.mongodb" % "mongo-java-driver" % versions.javaMongoDriver,
    //circe
    "de.heikoseeberger" %% "akka-http-circe" % versions.akkaHttpCirce,
    "io.circe" %% "circe-generic" % versions.circe,
    //akka
    "com.typesafe.akka" %% "akka-http" % versions.akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % versions.akkaHttpVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % versions.akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % versions.akkaVersion,
    //loggging
    "ch.qos.logback" % "logback-classic" % versions.logback,
    //test
    "com.typesafe.akka" %% "akka-http-testkit" % versions.akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % versions.akkaVersion % Test,
    "org.scalatest" %% "scalatest" % versions.scalaTest % Test,
    "com.github.fakemongo" % "fongo" % versions.fakeMongo % "test",
    "org.pegdown" % "pegdown" % versions.pegdown % "test"
  )
)
