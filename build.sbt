ThisBuild / scalaVersion := "2.13.13"

ThisBuild / version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """scala""",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test,

      // PostgreSQL driver
      "com.typesafe.play"  %% "play-slick"     % "5.2.0",
      "com.typesafe.slick" %% "slick-codegen"  % "3.4.1",
      "org.postgresql"      % "postgresql"     % "42.7.2",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",

      // MongoDB driver
      "org.mongodb.scala" %% "mongo-scala-driver" % "5.1.0",

      // JSON parser
      "io.circe" %% "circe-core"    % "0.14.7",
      "io.circe" %% "circe-generic" % "0.14.6",
      "io.circe" %% "circe-parser"  % "0.14.7",

      // Password encryption in database
      "org.mindrot" % "jbcrypt" % "0.4",

      // Authentication Token generator
      "com.pauldijou" %% "jwt-play" % "5.0.0",

      // Encryption Library
      "org.bouncycastle" % "bcpkix-jdk15on" % "1.70",

      // swagger
      "io.swagger.core.v3" % "swagger-core"        % "2.1.6",
      "io.swagger.core.v3" % "swagger-jaxrs2"      % "2.1.6",
      "io.swagger.core.v3" % "swagger-annotations" % "2.1.6",
      "io.swagger.core.v3" % "swagger-models"      % "2.1.6",

      // email sender
      "com.typesafe.play" %% "play-mailer"        % "9.0.0",
      "org.playframework" %% "play-mailer-guice"  % "10.0.0",

      "com.typesafe.akka" %% "akka-actor-typed" % "2.8.5",
      "com.typesafe.akka" %% "akka-slf4j" % "2.8.5",
      "com.typesafe.akka" %% "akka-serialization-jackson" % "2.8.5",
      "com.typesafe.akka" %% "akka-protobuf-v3" % "2.8.5",
      "com.typesafe.akka" %% "akka-stream" % "2.8.5",
    )
  )