ThisBuild / scalaVersion := "2.12.15"

sbtPlugin := true

enablePlugins(SbtPlugin)
import scala.collection.JavaConverters._
scriptedLaunchOpts += ("-Dproject.version=" + version.value)
scriptedLaunchOpts ++= java.lang.management.ManagementFactory.getRuntimeMXBean.getInputArguments.asScala.filter(a =>
  Seq("-Xmx", "-Xms", "-XX", "-Dfile").exists(a.startsWith)
)

crossSbtVersions := List("1.1.0")
organization     := "com.lightbend.paradox"
name             := "sbt-paradox-dependencies"

addSbtPlugin("com.lightbend.paradox" % "sbt-paradox"          % "0.4.3")
addSbtPlugin("net.virtual-void"      % "sbt-dependency-graph" % "0.10.0-RC1")

licenses += ("Apache-2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))
homepage := Some(url("https://github.com/lightbend/sbt-paradox-dependencies"))
scmInfo := Some(
  ScmInfo(
    url("https://github.com/lightbend/sbt-paradox-dependencies"),
    "git@github.com:lightbend/sbt-paradox-dependencies.git"
  )
)
developers += Developer(
  "contributors",
  "Contributors",
  "https://gitter.im/lightbend/paradox",
  url("https://github.com/lightbend/sbt-paradox-dependencies/graphs/contributors")
)
organizationName := "Lightbend Inc."
startYear        := Some(2018)

enablePlugins(AutomateHeaderPlugin)
scalafmtOnCompile := true

ThisBuild / githubWorkflowTargetTags ++= Seq("v*")
ThisBuild / githubWorkflowPublishTargetBranches :=
  Seq(RefPredicate.StartsWith(Ref.Tag("v")))
ThisBuild / githubWorkflowPublish := Seq(
  WorkflowStep.Sbt(
    List("ci-release"),
    env = Map(
      "PGP_PASSPHRASE" -> "${{ secrets.PGP_PASSPHRASE }}",
      "PGP_SECRET" -> "${{ secrets.PGP_SECRET }}",
      "SONATYPE_PASSWORD" -> "${{ secrets.SONATYPE_PASSWORD }}",
      "SONATYPE_USERNAME" -> "${{ secrets.SONATYPE_USERNAME }}"
    )
  )
)

ThisBuild / test / publishArtifact := false
ThisBuild / pomIncludeRepository   := (_ => false)
sonatypeProfileName                := "com.lightbend"

ThisBuild / githubWorkflowJavaVersions := List(
  JavaSpec.temurin("8"),
  JavaSpec.temurin("11"),
  JavaSpec.temurin("17")
)

ThisBuild / githubWorkflowTargetBranches := Seq("master", "main")
