import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}
import sbtrelease._

val shapelessV = "2.3.3"
val catsV = "2.2.0"
val scalaV = "2.13.2"

lazy val commonSettings = Seq(
  organization := "fede",
  scalaVersion := scalaV,
  crossScalaVersions := Seq(scalaVersion.value),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
  githubOwner := "fede0664",
  githubRepository := "pbdirect",  
  resolvers ++= Seq(
    Resolver.githubPackages("fede0664")
  ),
  externalResolvers += "ExampleLibrary packages" at "https://maven.pkg.github.com/fede0664",
)

lazy val pbdirect = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .withoutSuffixFor(JSPlatform)
  // .in(file("."))
  .settings(commonSettings,
    libraryDependencies += "com.chuusai"       %%% "shapeless"  % shapelessV,
    libraryDependencies += "org.typelevel"     %%% "cats-core"  % catsV,
  )
  .jvmSettings(
    libraryDependencies += "com.google.protobuf" % "protobuf-java"   % "3.13.0"
  )
  .jsSettings(
    libraryDependencies += "com.thesamet.scalapb" %%% "protobuf-runtime-scala" % "0.8.6"
  )

  lazy val root = project.in(file("."))
    .settings(commonSettings, skip in publish := true)
    .aggregate(pbdirect.jvm, pbdirect.js)