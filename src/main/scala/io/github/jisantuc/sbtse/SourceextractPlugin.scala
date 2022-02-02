package io.github.jisantuc.sbtse

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

import scala.util.Random

object SourceExtractPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = JvmPlugin

  object autoImport {
    val randomGenerator =
      SettingKey[Random]("random-generator", "random number generator")
    val randomNumber = TaskKey[Int]("random-number", "generate a random number")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    randomGenerator := new Random(),
    randomNumber := randomGenerator.value.nextInt(100)
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()
}
