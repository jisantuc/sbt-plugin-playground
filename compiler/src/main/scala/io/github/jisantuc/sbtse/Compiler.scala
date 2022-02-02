package io.github.jisantuc.sbtse

class CompilerJava {
  def compile(): Array[String] = {
    locally(Compiler().test)
    println("ok!")
    Array.empty
  }
}

final case class Compiler() {
  lazy val extracter = new Extracter()
  val test = extracter.testFn()
}
