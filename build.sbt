organization := "com.wistronics"
name := "repos"

version := "0.0.1-SNAPSHOT"
scalaVersion := "2.13.4"

addCommandAlias("fmt", "; compile:scalafmt; test:scalafmt; scalafmtSbt; compile:scalafix; test:scalafix;")
addCommandAlias(
  "fmtCheck",
  "; compile:scalafmtCheck; test:scalafmtCheck; scalafmtSbtCheck; compile:scalafix --check; test:scalafix --check;"
)

libraryDependencies ++= Seq(
  compilerPlugin(scalafixSemanticdb),
  "com.typesafe" % "config" % "1.4.1",
  "org.apache.commons" % "commons-lang3" % "3.4",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test
)

parallelExecution in Test := true
fork in Test := true
scalacOptions ++= Seq(
  "-Yrangepos", // required by SemanticDB compiler plugin
 // "-Ywarn-unused-import" // required by `RemoveUnused` rule
)
