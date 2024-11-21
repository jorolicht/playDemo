import scala.sys.process._ 

ThisBuild / scalaVersion := "3.3.3"
ThisBuild / organization := sys.env.getOrElse("APP_ORGANIZATION", "com.example")
ThisBuild / version      := sys.env.getOrElse("APP_VERSION", "0.0.1")
server / maintainer      := sys.env.getOrElse("APP_MAINTAINER", "Joe Doe <joe.doe@example.com>")

//ThisBuild / scalacOptions ++=Seq("-explain")

lazy val root = (project in file("."))
  .aggregate(server, client, shared.jvm, shared.js)

val concMsgFiles = taskKey[Unit]("Concatenate Message Files")  
lazy val server = project
  .settings(
    commands ++= Seq(info),
    concMsgFiles := {
      val msgFileDe = baseDirectory.value  / "conf" / "messages.de"
      val msgFileEn = baseDirectory.value  / "conf" / "messages.en"

      val filesDe = (baseDirectory.value / "conf" / "messages" / "de" ** "*.de").get
      val filesEn = (baseDirectory.value / "conf" / "messages" / "en" ** "*.en").get
      println(s"${msgFileDe}")
      IO.write(msgFileDe, filesDe.map(IO.read(_)).reduceLeft(_ ++ _))
      IO.write(msgFileEn, filesEn.map(IO.read(_)).reduceLeft(_ ++ _))
    },
    scalaJSProjects := Seq(client),
    Assets / pipelineStages  := Seq(scalaJSPipeline),
    pipelineStages := Seq(digest, gzip),
    // triggers scalaJSPipeline when using compile or continuous compilation
    Compile / compile := ((Compile / compile) dependsOn scalaJSPipeline).value,
    Compile / compile := ((Compile / compile) dependsOn concMsgFiles).value,
    
    libraryDependencies += guice,
    libraryDependencies += jdbc,
    libraryDependencies += evolutions,
    libraryDependencies += "com.mysql" % "mysql-connector-j" % "8.3.0",
    libraryDependencies += "org.postgresql" % "postgresql" % "42.5.4",
    libraryDependencies += "org.playframework.anorm" %% "anorm" % "2.7.0",
    libraryDependencies += "com.vmunier" %% "scalajs-scripts" % "1.3.0",
    libraryDependencies += "com.lihaoyi" %% "upickle" % "3.3.1",
    libraryDependencies += "com.google.api-client" % "google-api-client" % "2.4.0",
    libraryDependencies += "org.playframework" %% "play-mailer" % "10.0.0",
    libraryDependencies += "org.playframework" %% "play-mailer-guice" % "10.0.0",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0",
    libraryDependencies += "org.apache.pekko" %% "pekko-stream-typed" % "1.0.2"
  )
  .enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)
  .enablePlugins(SbtWeb)
  .dependsOn(shared.jvm)



lazy val client = project
  .settings(
    Compile / managedSources / excludeFilter := "addon",
    scalaJSUseMainModuleInitializer := false,
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "2.8.0",
    libraryDependencies += "com.lihaoyi" %%% "upickle" % "3.3.1",
    libraryDependencies += "org.rogach"  %%% "scallop" % "5.1.0",
    libraryDependencies += "org.typelevel" %%% "cats-core" % "2.12.0"
  )
  .enablePlugins(ScalaJSPlugin, ScalaJSWeb)
  .dependsOn(shared.js)
  .enablePlugins(SbtTwirl)


lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure).in(file("shared"))
  .settings(
     name := "shared",
     libraryDependencies ++= Seq(
       "com.lihaoyi" %%% "upickle" % "3.3.1",
       "com.lihaoyi" %% "upickle" % "3.3.1",
       "org.typelevel" %%% "shapeless3-deriving" % "3.4.0"
     )
   )
  .jsConfigure(_.enablePlugins(ScalaJSWeb))


// Add the following line to build.sbt if you wish to load the server project at sbt startup
// otherwise you have to switch to sbt> project server 
Global / onLoad := (Global / onLoad).value.andThen(state => "project server" :: state)

// clean will only delete the server's generated files (in the server/target directory). 
// Call root/clean to delete the generated files for all the projects.
// sbt 'set Global / scalaJSStage := FullOptStage' Universal/packageBin

//detailed error description
//set ThisBuild/scalacOptions ++=Seq("-explain")

// A simple, no-argument command that prints "Hi",
// leaving the current state unchanged.
def info = Command.command("info") { state =>
  println(s"Application Start Info")
  state
}