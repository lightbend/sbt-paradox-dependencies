# Releasing

1. Check [the travis build](https://travis-ci.com/lightbend/sbt-paradox-dependencies) has completed
1. Edit the [draft release](https://github.com/lightbend/sbt-paradox-dependencies/releases) and relase it with a tag like `v0.2`
1. Travis CI will start a [CI build](https://travis-ci.org/lightbend/sbt-paradox-dependencies/builds) for the new tag and publish artifacts to Bintray.
