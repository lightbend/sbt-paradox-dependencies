scalaVersion := "2.12.7"

sbtPlugin := true

enablePlugins(SbtPlugin)
import scala.collection.JavaConverters._
scriptedLaunchOpts += ("-Dproject.version=" + version.value)
scriptedLaunchOpts ++= java.lang.management.ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.filter(
  a => Seq("-Xmx", "-Xms", "-XX", "-Dfile").exists(a.startsWith)
)

crossSbtVersions := List("1.0.0")
organization     := "com.lightbend.paradox"
name             := "sbt-paradox-dependencies"

addSbtPlugin("com.lightbend.paradox" % "sbt-paradox"          % "0.4.3")
addSbtPlugin("net.virtual-void"      % "sbt-dependency-graph" % "0.9.2+10-148ba0ff")

resolvers += Resolver.bintrayIvyRepo("2m", "sbt-plugins")

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
homepage := Some(url("https://github.com/lightbend/sbt-paradox-dependencies"))
scmInfo := Some(
  ScmInfo(url("https://github.com/lightbend/sbt-paradox-dependencies"),
          "git@github.com:lightbend/sbt-paradox-dependencies.git"))
developers += Developer(
  "contributors",
  "Contributors",
  "https://gitter.im/lightbend/paradox",
  url("https://github.com/lightbend/sbt-paradox-dependencies/graphs/contributors")
)
organizationName := "Lightbend Inc."
startYear        := Some(2018)

bintrayOrganization := Some("sbt")
bintrayRepository   := "sbt-plugin-releases"

enablePlugins(AutomateHeaderPlugin)
scalafmtOnCompile := true
