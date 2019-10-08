/*
 * Copyright 2018 Lightbend Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lightbend.paradox.dependencies

import com.lightbend.paradox.markdown.Writer
import com.lightbend.paradox.sbt.ParadoxPlugin
import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport.paradoxDirectives
import net.virtualvoid.sbt.graph.{DependencyGraphKeys, ModuleTree}
import sbt._
import sbt.Keys._

object ParadoxDependenciesPlugin extends AutoPlugin {
  object autoImport extends ParadoxDependenciesPluginKeys
  import autoImport._

  override def requires: Plugins = ParadoxPlugin

  override def trigger: PluginTrigger = AllRequirements

  override def projectSettings: Seq[Setting[_]] = dependenciesSettings(Compile)

  def dependenciesZeroSettings: Seq[Setting[_]] = Seq(
    paradoxDependenciesShowLicenses := {
      sbtVersion.value.startsWith("1.1.") || sbtVersion.value.startsWith("1.2.")
    },
    paradoxDependenciesModuleTrees := Def.taskDyn {
          val projectsToFilter = paradoxDependenciesProjects.?.value
            .map(inProjects)
            .getOrElse {
              inAggregates(LocalRootProject, includeRoot = true)
            }

          val filter: ScopeFilter = ScopeFilter(projectsToFilter, inConfigurations(Compile))

          val projectIdWithTree = Def.task {
            (thisProject.value.id, ModuleTree(DependencyGraphKeys.moduleGraphSbt.value))
          }

          projectIdWithTree.all(filter).map(_.toMap)
        }.value,
    paradoxDirectives ++= Def.taskDyn {
          Def.task {
            val trees = paradoxDependenciesModuleTrees.value
            Seq(
              { _: Writer.Context ⇒
                new DependenciesDirective(paradoxDependenciesShowLicenses.value)(projectId => {
                  trees.get(projectId) match {
                    case Some(deps) => deps
                    case _ => throw new Error(s"Could not retrieve dependency information for project [$projectId]")
                  }
                })
              }
            )
          }
        }.value
  )

  def dependenciesSettings(config: Configuration): Seq[Setting[_]] =
    dependenciesZeroSettings ++ inConfig(config)(
          Seq(
            // scoped settings here
          )
        )
}
