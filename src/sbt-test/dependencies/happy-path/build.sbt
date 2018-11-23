enablePlugins(ParadoxPlugin)
paradoxTheme := None
paradoxDependenciesModules := Seq(thisProjectRef.value)

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.17"
