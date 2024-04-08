ThisBuild / scalaVersion := "2.13.13"

ThisBuild / version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """scala""",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
      "com.typesafe.play" %% "play-slick" % "5.2.0",
      "com.typesafe.slick" %% "slick-codegen" % "3.4.1",
      "com.typesafe.play" %% "play-json" % "2.10.4",
      "org.postgresql" % "postgresql" % "42.7.2",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
      "com.lihaoyi" %% "upickle" % "3.2.0",
      "org.mindrot" % "jbcrypt" % "0.4",
      "org.fusesource.jansi" % "jansi" % "2.4.1",
      "org.scalatest" %% "scalatest" % "3.2.17" % "test",
      specs2 % Test
    )
  )