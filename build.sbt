name := """prefcards"""
organization := "com.example"
version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.15"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-slick" % "5.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "5.0.0",
  "org.postgresql" % "postgresql" % "42.5.0",
  "com.typesafe.slick" %% "slick" % "3.3.2",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.3.2",
  "org.flywaydb" %% "flyway-play" % "9.1.0",
  "org.hsqldb" % "hsqldb" % "2.5.0",
  "com.iterable" %% "swagger-play" % "2.0.1",
  "com.iheart" %% "play-swagger" % "0.10.8",
  "org.reflections" % "reflections" % "0.9.8" notTransitive(),
//  "com.github.swagger-akka-http" %% "swagger-scala-module" % "2.13.0",
//  "com.github.swagger-akka-http" %% "swagger-akka-http" % "2.4.0",
//  "io.swagger.core.v3" % "swagger-annotations" % "2.2.10",
 "org.webjars" %% "webjars-play" % "2.8.0",
  "org.webjars" % "swagger-ui" % "3.52.5",
  "com.typesafe.play" %% "play-json" % "2.9.2",
//  "com.fasterxml.jackson.core" % "jackson-databind" % "2.12.6",
//  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.6",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
)



libraryDependencies += guice
dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "2.2.0"
resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.mavenCentral
resolvers += Resolver.jcenterRepo





