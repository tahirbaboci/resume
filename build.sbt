lazy val akkaHttpVersion = "10.1.11"
lazy val akkaVersion = "2.6.5"
lazy val circe = "0.13.0"

lazy val root = (project in file(".")).settings(
  resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/",
  resolvers += "Sonatype releases" at "https://oss.sonatype.org/content/repositories/releases",
  inThisBuild(List(organization := "com.baboci", scalaVersion := "2.13.1")),
  name := "Resume",
  libraryDependencies ++= Seq(
    //mongo
    "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0",
    "org.mongodb" % "mongo-java-driver" % "3.12.3",
    //circe
    "de.heikoseeberger" %% "akka-http-circe" % "1.32.0",
    "io.circe" %% "circe-generic" % circe,
    //akka
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    //loggging
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    //test
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test,
    "com.typesafe.akka" %% "akka-actor-testkit-typed" % akkaVersion % Test,
    "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    "com.github.fakemongo" % "fongo" % "2.1.1" % "test",
    "org.pegdown" % "pegdown" % "1.6.0" % "test"
  )
)
