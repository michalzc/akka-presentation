lazy val akkaVersion = "2.3.9"
lazy val springBootVersion = "1.2.2.RELEASE"
lazy val sprayVersion = "1.3.2"

scalaVersion := "2.11.5"

lazy val commonSettings = Seq(
  organization := "michalz",
  version := "0.1.0",
  scalaVersion := "2.11.5",
  resolvers ++= Seq(
    DefaultMavenRepository,
    Resolver.sonatypeRepo("public"),
    Resolver.typesafeRepo("releases")
  ),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,

    //test dependencies
    "junit" % "junit" % "4.12" % "test",
    "org.mockito" % "mockito-all" % "1.10.19" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaVersion % "test"
  ),
  dependencyOverrides += "org.scala-lang" % "scala-compiler" % scalaVersion.value
) ++ net.virtualvoid.sbt.graph.Plugin.graphSettings


lazy val javaAkka = (project in file("java-akka"))
  .settings(commonSettings: _*)
  .settings(
    name := "Java akka project",
    libraryDependencies ++= Seq(
      "org.springframework.boot" % "spring-boot-starter-web" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-data-mongodb" % springBootVersion,
      "com.google.guava" % "guava" % "18.0",
      "org.projectlombok" % "lombok" % "1.16.2",
      "commons-io" % "commons-io" % "2.4"
    )
  )

lazy val sacScala = (project in file("sac-scala"))
  .settings(commonSettings: _*)
  .settings(Revolver.settings: _*)
  .settings(
    name := "Simple Scala Sac",
    libraryDependencies ++= Seq(
      "org.reactivemongo" %% "reactivemongo" % "0.10.5.0.akka23",
      "io.spray" %% "spray-can" % sprayVersion,
      "io.spray" %% "spray-routing" % sprayVersion,
      "org.json4s" %% "json4s-jackson" % "3.2.10",
      "org.slf4j" % "slf4j-api" % "1.7.10",
      "org.apache.logging.log4j" % "log4j-api" % "2.1",
      "org.apache.logging.log4j" % "log4j-core" % "2.1",
      "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.1",
      "org.apache.commons" % "commons-csv" % "1.1",

      //test dependencies
      "io.spray" %% "spray-testkit" % "1.3.2" % "test"
    )
  )
