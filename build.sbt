ThisBuild / tlBaseVersion := "0.0"

ThisBuild / organization := "de.commercetools"
ThisBuild / organizationName := "Commercetools GmbH"
ThisBuild / startYear := Some(2024)
ThisBuild / tlCiHeaderCheck := false
ThisBuild / tlCiDependencyGraphJob := false
ThisBuild / developers := List(
  tlGitHubDev("satabin", "Lucas Satabin")
)

val Scala213 = "2.13.12"
ThisBuild / crossScalaVersions := Seq(Scala213, "3.3.1")
ThisBuild / scalaVersion := Scala213

lazy val root = tlCrossRootProject.aggregate(core, azureServiceBus)

val commonSettings = List(
  libraryDependencies ++= Seq(
    "co.fs2" %%% "fs2-core" % Versions.fs2,
    "org.scalameta" %%% "munit" % Versions.munit % Test,
    "org.typelevel" %%% "munit-cats-effect-3" % Versions.munitCatsEffect % Test
  )
)

lazy val core = crossProject(JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .settings(commonSettings)
  .settings(
    name := "cloud-queues-core"
  )

lazy val queuesCirce = crossProject(JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("circe"))
  .settings(commonSettings)
  .settings(
    name := "cloud-queues-circe"
  )

lazy val azureServiceBus = crossProject(JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("azure/service-bus"))
  .settings(commonSettings)
  .settings(
    name := "cloud-queues-azure-service-bus",
    libraryDependencies ++= List(
      "com.azure" % "azure-messaging-servicebus" % "7.0.0",
      "co.fs2" %%% "fs2-reactive-streams" % Versions.fs2
    )
  )
  .dependsOn(core)

lazy val awsSQS = crossProject(JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("aws/sqs"))
  .settings(commonSettings)
  .settings(
    name := "cloud-queues-aws-sqs",
    libraryDependencies ++= List(
      "io.laserdisc" %%% "fs2-aws-sqs" % "6.1.1"
    )
  )
  .dependsOn(core)

lazy val readme = project
  .in(file("readme"))
  .enablePlugins(MdocPlugin, NoPublishPlugin)
  .settings(
    mdocOut := file("."),
    libraryDependencies ++= List(
      "com.azure" % "azure-identity" % "1.11.1"
    ))
  .dependsOn(azureServiceBus.jvm)
