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
  "org.reflections" % "reflections" % "0.9.8" notTransitive(),
  "org.webjars" %% "webjars-play" % "2.8.0",
  "com.typesafe.play" %% "play-json" % "2.9.2",
  "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,

  "com.github.dwickern" %% "swagger-play3.0" % "4.0.0",

);


libraryDependencies += guice
dependencyOverrides += "org.scala-lang.modules" %% "scala-xml" % "2.2.0"
resolvers += Resolver.sonatypeRepo("releases")
resolvers += Resolver.mavenCentral
resolvers += Resolver.jcenterRepo






