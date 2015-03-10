name := "JVerbNet"

// Since this is a Java project, we don't need the scala version _2.10 or _2.11
// suffix on our package name.
crossPaths := false

organization := "edu.mit"

publishMavenStyle := true

publishArtifact in Test := false

pomIncludeRepository := { _ => false }

licenses := Seq("Creative Commons Attribution Version 3.0 Unported" -> new URL("http://creativecommons.org/licenses/by/3.0/legalcode"))

homepage := Some(url("http://projects.csail.mit.edu/jverbnet/"))

scmInfo := Some(ScmInfo(
    url("https://github.com/allenai/jverbnet"),
    "https://github.com/allenai/jverbnet.git"))

ReleaseKeys.publishArtifactsAction := PgpKeys.publishSigned.value

pomExtra :=
    <developers>
      <developer>
        <id>allenai-dev-role</id>
        <name>Allen Institute for Artificial Intelligence</name>
        <email>dev-role@allenai.org</email>
      </developer>
    </developers>

dependencyOverrides += "org.apache.commons" % "commons-compress" % "1.7"

version := "1.2.0"

PublishTo.sonatype
