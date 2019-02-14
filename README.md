# sbt-paradox-dependencies [![bintray-badge][]][bintray] [![travis-badge][]][travis]

A [Paradox](https://github.com/lightbend/paradox/) directive to show module's transitive dependencies in the Paradox generated documentation.

## Usage 

```scala
addSbtPlugin("com.lightbend.paradox" % "sbt-paradox-dependencies" % <latest>)
```

Use the directive in a Paradox markdown file and specify the sbt project id.

```
@@dependencies { projectId="core" }
```

Any project from the build root project aggregates can be specified. If the project is not among the aggregates, then it needs
to be added to the `paradoxDependenciesProjects` setting value.

## License

The license is Apache 2.0, see LICENSE.

## Maintenance notes

**This project is NOT supported under the Lightbend subscription.**

Feel free to ping contributors for code review or discussions. Pull requests are very welcome–thanks in advance!

[bintray]:               https://bintray.com/sbt/sbt-plugin-releases/sbt-paradox-dependencies
[bintray-badge]:         https://api.bintray.com/packages/sbt/sbt-plugin-releases/sbt-paradox-dependencies/images/download.svg
[travis]:                https://travis-ci.com/lightbend/sbt-paradox-dependencies
[travis-badge]:          https://travis-ci.com/lightbend/sbt-paradox-dependencies.svg?branch=master

