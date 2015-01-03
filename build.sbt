name := "JVerbNet"

organization := "edu.mit"

addSbtPlugin("org.allenai.plugins" % "allenai-sbt-plugins" % "2014.11.25-0")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

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
