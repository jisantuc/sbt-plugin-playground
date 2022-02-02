# sbt-source-extract

Attempt to replicate errors in another open source CI build in a fresh setup

The error in question occurs at runtime in the [first line of `extractAll`](https://github.com/scala-exercises/sbt-exercise/blob/fc3f124fe7005c1675bec1ee6d4c3feb916a6a3f/compiler/src/main/scala/org/scalaexercises/exercises/compiler/SourceTextExtraction.scala#L63-L65)
in the `scala-exercises/sbt-exercise` repo. It looks like this:

```
[info]   java.lang.AssertionError: assertion failed: 
[info]   No RuntimeVisibleAnnotations in classfile with ScalaSignature attribute: package object collection
[info]      while compiling: <no file>
[info]         during phase: globalPhase=<no phase>, enteringPhase=<some phase>
[info]      library version: version 2.12.15
[info]     compiler version: version 2.12.15
[info]   reconstructed args: -bootclasspath /home/runner/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-compiler/2.12.15/scala-compiler-2.12.15.jar:/home/runner/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.15/scala-library-2.12.15.jar -usejavacp -classpath /home/runner/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-compiler/2.12.15/scala-compiler-2.12.15.jar:/home/runner/.cache/coursier/v1/https/repo1.maven.org/maven2/org/scala-lang/scala-library/2.12.15/scala-library-2.12.15.jar -Yrangepos
[info] 
[info]   last tree to typer: EmptyTree
[info]        tree position: <unknown>
[info]             tree tpe: <notype>
[info]               symbol: null
[info]            call site: <none> in <none>
[info] 
[info] == Source file context for tree position ==
[info]   at scala.reflect.internal.SymbolTable.throwAssertionError(SymbolTable.scala:185)
[info]   at scala.tools.nsc.symtab.classfile.ClassfileParser.unpickleOrParseInnerClasses(ClassfileParser.scala:1168)
[info]   at scala.tools.nsc.symtab.classfile.ClassfileParser.parseClass(ClassfileParser.scala:468)
[info]   at scala.tools.nsc.symtab.classfile.ClassfileParser.$anonfun$parse$2(ClassfileParser.scala:161)
[info]   at scala.tools.nsc.symtab.classfile.ClassfileParser.$anonfun$parse$1(ClassfileParser.scala:147)
[info]   at scala.tools.nsc.symtab.classfile.ClassfileParser.parse(ClassfileParser.scala:130)
[info]   at scala.tools.nsc.symtab.SymbolLoaders$ClassfileLoader.doComplete(SymbolLoaders.scala:343)
[info]   at scala.tools.nsc.symtab.SymbolLoaders$SymbolLoader.complete(SymbolLoaders.scala:250)
[info]   at scala.reflect.internal.Symbols$Symbol.completeInfo(Symbols.scala:1542)
[info]   at scala.reflect.internal.Symbols$Symbol.info(Symbols.scala:1514)
```

I don't know what that means, and there's a lot going on in that plugin, so this repo attempts to get to a reproduction
with way less going on.

## Usage

This plugin requires sbt 1.0.0+

### Testing

Run `test` for regular unit tests.

Run `scripted` for [sbt script tests](http://www.scala-sbt.org/1.x/docs/Testing-sbt-plugins.html).

### CI

The generated project uses [sbt-github-actions](https://github.com/djspiewak/sbt-github-actions) as a plugin to generate workflows for GitHub actions. For full details of how to use it [read this](https://github.com/djspiewak/sbt-github-actions/blob/main/README.md)