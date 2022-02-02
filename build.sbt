name := """sbt-source-extract"""
version := "0.1-SNAPSHOT"

sbtPlugin := true

addCommandAlias(
  "ci-test",
  ";scalafmtCheckAll; scalafmtSbtCheck; +test; +publishLocal; scripted"
)

// ScalaTest
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test

inThisBuild(
  List(
    organization := "io.github.jisantuc",
    homepage := Some(url("https://github.com/jisantuc/sbt-plugin-playground")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "jisantuc",
        "James Santucci",
        "james.santucci@47deg.com",
        url("https://github.com/jisantuc")
      )
    )
  )
)

(console / initialCommands) := """import io.github.jisantuc.sbtse._"""

enablePlugins(ScriptedPlugin, SbtPlugin)
// set up 'scripted; sbt plugin for testing sbt plugins
scriptedLaunchOpts ++=
  Seq("-Xmx1024M", "-Dplugin.version=" + version.value)

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(List("ci-test"))
)

ThisBuild / githubWorkflowPublishTargetBranches := Seq.empty