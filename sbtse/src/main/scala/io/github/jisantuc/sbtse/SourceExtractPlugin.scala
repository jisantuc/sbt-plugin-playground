package io.github.jisantuc.sbtse

import sbt._
import sbt.Keys._
import sbt.plugins.JvmPlugin

import cats.data.Ior
import cats.syntax.either._
import cats.syntax.monadError._
import scala.util.Random
import sbt.internal.inc.classpath.ClasspathUtil
import java.nio.file.Paths

object SourceExtractPlugin extends AutoPlugin {

  override def trigger = allRequirements
  override def requires = JvmPlugin

  object autoImport {
    val testInit =
      TaskKey[Unit]("test-init", "run initialization to see if it crashes")
  }

  import autoImport._

  override lazy val projectSettings = Seq(
    testInit := locally(testTask.value)
  )

  override lazy val buildSettings = Seq()

  override lazy val globalSettings = Seq()

  private def catching[A](f: => A)(msg: => String) =
    Either.catchNonFatal(f).leftMap(e => Ior.both(msg, e))

  private val COMPILER_CLASS = "io.github.jisantuc.sbtse.CompilerJava"

  private type COMPILER = {
    def compile(): Array[String]
  }

  def testTask = Def.task {
    val libraryClasspath = Attributed.data((Compile / fullClasspath).value)
    val classpath = (Meta.compilerClasspath ++ libraryClasspath).distinct
    val loader = ClasspathUtil.toLoader(
      classpath.map(file => Paths.get(file.getAbsolutePath())),
      null,
      ClasspathUtil.createClasspathResources(
        appPaths =
          Meta.compilerClasspath.map(file => Paths.get(file.getAbsolutePath())),
        bootPaths = scalaInstance.value.allJars.map(file =>
          Paths.get(file.getAbsolutePath())
        )
      )
    )

    val compilerClass = loader.loadClass(COMPILER_CLASS)
    val compiler = compilerClass.newInstance.asInstanceOf[COMPILER]
    compiler.compile()
  }
}
