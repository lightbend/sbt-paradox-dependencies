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

  def dependenciesGlobalSettings: Seq[Setting[_]] = Seq(
    paradoxDirectives ++= Def.taskDyn {
      Def.task {
        val s = state.value
        Seq(
          { _: Writer.Context ⇒
            new DependenciesDirective(projectId => {
              Project.runTask(LocalProject(projectId) / Compile / DependencyGraphKeys.moduleGraphSbt, s) match {
                case Some((_, Value(deps))) => ModuleTree(deps)
                case _ => throw new Error(s"Could not retrieve dependency information for projectId [$projectId]")
              }
            })
          }
        )
      }
    }.value
  )

  def dependenciesSettings(config: Configuration): Seq[Setting[_]] =
    dependenciesGlobalSettings ++ inConfig(config)(
      Seq(
        // scoped settings here
      ))
}
