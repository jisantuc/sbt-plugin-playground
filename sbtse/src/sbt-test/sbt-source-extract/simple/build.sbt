version := "0.1"
scalaVersion := "2.12.15"

lazy val simple = (project in file("."))
  .settings(
    resolvers ++= Seq(
      Resolver.sonatypeRepo("snapshots"),
      Resolver.defaultLocal
    )
  )
  .enablePlugins(SourceExtractPlugin)
