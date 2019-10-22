name := """play-scala-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
libraryDependencies += "com.h2database" % "h2" % "1.4.192"
libraryDependencies += jdbc

libraryDependencies ++= Seq(
  ws,
  "org.mindrot" % "jbcrypt" % "0.3m",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
