# sbt-paradox-dependencies [![maven-central-badge][]][maven-central][![github-actions-badge][]][github-actions]

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

```scala
paradoxDependenciesProjects := Seq(projectA, projectB)
```

## License

The license is Apache 2.0, see LICENSE.

## Maintenance notes

**This project is NOT supported under the Lightbend subscription.**

Feel free to ping contributors for code review or discussions. Pull requests are very welcome–thanks in advance!

[maven-central]:         https://maven-badges.herokuapp.com/maven-central/com.lightbend.paradox/sbt-paradox-dependencies
[maven-central-badge]:   https://maven-badges.herokuapp.com/maven-central/com.lightbend.paradox/sbt-paradox-dependencies/badge.svg
[github-actions]:        https://github.com/lightbend/sbt-paradox-dependencies/actions/workflows/ci.yml?query=branch%3Amain
[github-actions-badge]:  https://github.com/lightbend/sbt-paradox-dependencies/actions/workflows/ci.yml/badge.svg?branch=main
