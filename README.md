JVerbNet
========

AI2 is not the author of this code. That credit goes to MIT and http://projects.csail.mit.edu/jverbnet/. We are just hosting it here so we can publish it to Maven Central, and depend on it in our projects.

Here is a description from their website:

> JVerbnet is a Java library for interfacing with the University of Colorado's Verbnet data. It features API calls to retrieve verb classes by their id numbers, associated Wordnet keys or Propbank groupings. The library includes no GUI elements, and is written for Java 1.5.0. The software is free to use for all purposes, as long as proper acknowledgments are made.

> JVerbnet does not include the verbnet data directly. The Verbnet data itself must be downloaded separately from the Verbnet site at http://verbs.colorado.edu/~mpalmer/projects/verbnet.html.

Publishing a new version
------------------------

Since AI2 is a Scala shop, we wrapped this in sbt to use our publishing tools, even though JVerbNet is a pure Java project.

To release to Maven Central, you need a bit of setup:

 1. You need the signing keys to publish software with. You can find them in the `ai2-secure` bucket in S3 under the key `Sonatype Key Pair.zip`. Copy that file to `~/.sbt/gpg/` and extract it there.
 2. You need the passphrase for that key pair. It's defined as an array, which is a little weird, and goes into another location in `~/.sbt`. The line defining it is in `passwords.txt` in the `ai2-secure` bucket. Copy that line into `~/.sbt/0.13/allenai.sbt` (or into some other `.sbt` if you like).
 3. To use the passphrase, we have to enable the `sbt-pgp` plugin. Put the following line into `~/.sbt/0.13/plugins/gpg.sbt`: `addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")`
 4. We also need credentials to the sonatype repository. We get those with the following line in `~/.sbt/0.13/sonatypt.sbt`: `credentials += Credentials("Sonatype Nexus Repository Manager", "oss.sonatype.org", "allenai-role", "<password>")`. You find this password in the same `password.txt` file from above.

Now, you need to register your GPG key.

1. Start SBT in the common project
2. At the SBT prompt, type:

   ```bash
   > pgp-cmd send-key [TAB]
   Paul Allen Institute for Artificial Intelligence <account>
   abcdefg
   ```
 
   When you hit [TAB], SBT should print out the available key and its ID on the second line (in the example above, `abcdefg`. Enter the id:
 
   ```bash
   > pgp-cmd send-key abcdefg hkp://keyserver.ubuntu.com [ENTER]
   ```

With this, you should be ready to run `sbt publishSigned`. When you do, it will upload the build artifacts to a staging repository on http://oss.sonatype.org. When it's done, you have to go there and first close, and then release the staging repository. That initiates the upload to Maven Central, which will take about one minute.

 1. Go to http://oss.sonatype.org.
 2. Log in with username `allenai-role`, and the password from the `password.txt` file. This is the same password you used in step 4 above.
 3. Click "staging repositories" on the left.
 4. Use the search bar at the top right to search for "allenai".
 5. Find your staging repository and confirm that it has the contents you expect. Then, select it and click "Close". Closing takes a few minutes. Then you can see how the closing process went under "Activity". It sends an email to `dev-role@allenai.org` when it's done.
 6. When it is done, select the repository again and hit "Release".
 7. You should see the new version appear under https://oss.sonatype.org/content/repositories/releases/edu/mit/jverbnet

You are done!
