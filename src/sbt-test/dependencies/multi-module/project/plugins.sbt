addSbtPlugin("com.lightbend.paradox" % "sbt-paradox-dependencies" % sys.props("project.version"))
resolvers += Resolver.bintrayIvyRepo("2m", "sbt-plugins")
