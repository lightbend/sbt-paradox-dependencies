# sbt-paradox-dependencies 

# End-of-life -- this plugin is not maintained anymore, the repository will soon be archived

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

End-of-life -- this plugin is not maintained anymore, the repository will soon be archived

**This project is NOT supported under the Lightbend subscription.**
