package io.github.jisantuc.sbtse

import java.io.File

import scala.annotation.tailrec
import scala.collection.compat._
import scala.reflect.internal.util.BatchSourceFile
import scala.tools.nsc._
import scala.tools.nsc.doc.{Settings => _, _}
import scala.tools.nsc.doc.base.comment.Comment

class Extracter {
  private def classPathOfClass(className: String): List[String] = {
    val resource = className.split('.').mkString("/", "/", ".class")
    val path = getClass.getResource(resource).getPath
    if (path.indexOf("file:") >= 0) {
      val indexOfFile = path.indexOf("file:") + 5
      val indexOfSeparator = path.lastIndexOf('!')
      List(path.substring(indexOfFile, indexOfSeparator))
    } else {
      require(path.endsWith(resource))
      List(path.substring(0, path.length - resource.length + 1))
    }
  }

  private lazy val compilerPath =
    try classPathOfClass("scala.tools.nsc.Interpreter")
    catch {
      case e: Throwable =>
        throw new RuntimeException(
          "Unable to load Scala interpreter from classpath (scala-compiler jar is missing?)",
          e
        )
    }

  private lazy val libPath =
    try classPathOfClass("scala.AnyVal")
    catch {
      case e: Throwable =>
        throw new RuntimeException(
          "Unable to load scala base object from classpath (scala-library jar is missing?)",
          e
        )
    }

  lazy val paths: List[String] = compilerPath ::: libPath

  // Same settings as
  // https://github.com/scala-exercises/sbt-exercise/blob/v0.6.7/compiler/src/main/scala/org/scalaexercises/exercises/compiler/SourceTextExtraction.scal
  // and
  // https://github.com/scala-exercises/sbt-exercise/blob/v0.6.7/compiler/src/main/scala/org/scalaexercises/exercises/compiler/CompilerSettings.scala
  // but with a vanilla Global as the embeddedDefaults type param
  val defaultSettings = new Settings {
    embeddedDefaults[Global.type]
    Yrangepos.value = true
    usejavacp.value = true

    bootclasspath.value = paths.mkString(File.pathSeparator)
    classpath.value = paths.mkString(File.pathSeparator)
  }
  val global = new Global(defaultSettings)

  def testFn(): global.Run = {
    new global.Run()
  }
}
