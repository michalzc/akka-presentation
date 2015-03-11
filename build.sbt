lazy val akkaVersion = "2.3.9"
lazy val springBootVersion = "1.2.2.RELEASE"

scalaVersion := "2.11.5"

lazy val commonSettings = Seq(
  organization := "michalz",
  version := "0.1.0",
  scalaVersion := "2.11.5",
  resolvers ++= Seq(
    DefaultMavenRepository,
    Resolver.sonatypeRepo("public")
  ),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaVersion
  )
)


lazy val javaAkka = (project in file("java-akka"))
  .settings(commonSettings: _*)
  .settings(
    name := "Java akka project",
    libraryDependencies ++= Seq(
      "org.springframework.boot" % "spring-boot-starter-web" % springBootVersion,
      "org.springframework.boot" % "spring-boot-starter-data-mongodb" % springBootVersion
    )
  )
