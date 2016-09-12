organization := "com.passivsystems"

name := "embed"

version := "0.0.2-SNAPSHOT"

scalaVersion :=  "2.11.7"

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-reflect" % _)  // for scala macro

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0" % Test
