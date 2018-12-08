enablePlugins(ParadoxPlugin)
paradoxTheme := None

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.17"

lazy val projectA = project
  .settings(
    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.17"
  )

lazy val projectB = Project("projectBee", file("bee"))
  .dependsOn(
    projectA
  )
  .settings(
    libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.25"
  )

lazy val multiModule = project
  .aggregate(
    projectB
  )
