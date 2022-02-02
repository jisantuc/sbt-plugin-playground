name := """sbt-source-extract"""
version := "0.1-SNAPSHOT"

sbtPlugin := true

addCommandAlias(
  "ci-test",
  ";scalafmtCheckAll; scalafmtSbtCheck; +test; +publishLocal; scripted"
)

lazy val root = project.aggregate(compiler, `sbt-source-extract`)

lazy val dependencies = Seq(
  "org.scalactic" %% "scalactic" % "3.2.9" % Test,
  "org.scalatest" %% "scalatest" % "3.2.9" % Test,
  "org.typelevel" %% "cats-core" % "2.4.0"
)

lazy val compilerClasspath = TaskKey[Classpath]("compiler-classpath")

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
        "james.santucci@47d1eg.com",
        url("https://github.com/jisantuc")
      )
    )
  )
)

(console / initialCommands) := """import io.github.jisantuc.sbtse._"""

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(List("ci-test"))
)

ThisBuild / githubWorkflowPublishTargetBranches := Seq.empty

lazy val `sbt-source-extract` = (project in file("."))
  .settings(
    compilerClasspath := { (compiler / Compile / fullClasspath) }.value,
    buildInfoObject := "Meta",
    buildInfoPackage := "io.github.jisantuc.sbtse",
    buildInfoKeys := Seq(
      version,
      BuildInfoKey.map(compilerClasspath) { case (_, classFiles) ⇒
        ("compilerClasspath", classFiles.map(_.data))
      }
    ),
    libraryDependencies ++= dependencies,
    // set up 'scripted; sbt plugin for testing sbt plugins
    scriptedLaunchOpts ++=
      Seq("-Xmx1024M", "-Dplugin.version=" + version.value)
  )
  .dependsOn(compiler)

lazy val compiler = (project in file("compiler"))
  .settings(
    libraryDependencies ++= dependencies
  )
  .enablePlugins(BuildInfoPlugin, SbtPlugin)
