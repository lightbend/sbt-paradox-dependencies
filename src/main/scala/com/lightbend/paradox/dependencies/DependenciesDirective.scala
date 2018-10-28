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

import com.lightbend.paradox.markdown.LeafBlockDirective
import net.virtualvoid.sbt.graph.{ModuleTree, ModuleTreeNode}
import org.pegdown.Printer
import org.pegdown.ast.{DirectiveNode, VerbatimGroupNode, VerbatimNode, Visitor}

class DependenciesDirective(nameToDependencies: String => ModuleTree) extends LeafBlockDirective("dependencies") {
  def render(node: DirectiveNode, visitor: Visitor, printer: Printer): Unit = {
    val moduleName = node.attributes.value("module")
    val tree       = nameToDependencies(moduleName)
    val sb         = StringBuilder.newBuilder
    tree.roots.foreach(renderNode(sb, "", _, printer))
    new VerbatimNode(sb.toString, "").accept(visitor)
  }

  private def renderNode(sb: StringBuilder, indent: String, n: ModuleTreeNode, printer: Printer): Unit =
    if (n.node.evictedByVersion.isEmpty) {
      val moduleId = n.node.id
      val name     = moduleId.name
      sb.append(indent)
        .append('"')
        .append(moduleId.organisation)
        .append('"')
        .append(" ")
        .append(name)
        .append(" ")
        .append(moduleId.version)
      n.node.license.foreach(l => sb.append(" //").append(l))
      sb.append("\n")
      n.children.foreach(renderNode(sb, indent + "   ", _, printer))
    }
}
