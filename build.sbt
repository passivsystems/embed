organization := "com.passivsystems"

name := "embed"

version := "0.0.1"

scalaVersion :=  "2.11.7"

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _)  // for scala macro