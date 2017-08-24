name := """url-alias-user"""
organization := "com.ruchij"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.3"


libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.0" % Test
libraryDependencies += "org.reactivemongo" % "reactivemongo_2.12" % "0.12.5"
libraryDependencies += "com.github.etaty" % "rediscala_2.12" % "1.8.0"


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.ruchij.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.ruchij.binders._"
