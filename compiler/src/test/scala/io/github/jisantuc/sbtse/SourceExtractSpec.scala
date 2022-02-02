package io.github.jisantuc.sbtse

import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

class SourceExtractTest extends AnyFunSpec with Matchers {
  describe("run without crashing when initialized without classpath hackery") {
    it("should do that") {
      new CompilerJava().compile()
    }
  }
}
