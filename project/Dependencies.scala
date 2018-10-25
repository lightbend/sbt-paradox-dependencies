import sbt._

object Version {
  val sbtParadox      = "0.4.3"
  val dependencyGraph = "0.9.2+10-148ba0ff"
}

object Library {
  val sbtParadox      = "com.lightbend.paradox" % "sbt-paradox"          % Version.sbtParadox
  val dependencyGraph = "net.virtual-void"      % "sbt-dependency-graph" % Version.dependencyGraph
}
