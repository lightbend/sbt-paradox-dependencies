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
import net.virtualvoid.sbt.graph.{Module, ModuleGraph} // .graph.{ModuleTree, ModuleTreeNode}
import org.pegdown.Printer
import org.pegdown.ast.{DirectiveNode, Visitor}

class DependenciesDirective(showLicenses: Boolean)(projectIdToDependencies: String => ModuleGraph)
    extends LeafBlockDirective("dependencies") {
  def render(node: DirectiveNode, visitor: Visitor, printer: Printer): Unit = {
    val projectId = node.attributes.value("projectId")
    val graph     = projectIdToDependencies(projectId)
    printer.println()
    val classes = Seq("dependencies", node.attributes.classesString).filter(_.nonEmpty)
    printer.print(s"""<dl class="${classes.mkString(" ")}">""")
    if (graph.roots.flatMap(m => children(graph, m)).nonEmpty) {
      renderDirect(graph, showLicenses, printer)
      renderTree(graph, printer)
    } else {
      printer.print("<dt>Direct dependencies</dt><dd>This module has no dependencies.</dd>")
    }
    printer.print("</dl>")
    printer.println()
  }

  private def renderDirect(graph: ModuleGraph, showLicenses: Boolean, p: Printer): Unit = {
    p.print("<dt>Direct dependencies</dt><dd><table>")
    p.indent(2).println()
    p.print("<thead><tr><th>Organization</th><th>Artifact</th><th>Version</th>")
    if (showLicenses) p.print("<th>License</th></tr>")
    p.print("</thead>").println()
    p.print("<tbody>")
    p.indent(2)
    for {
      r <- graph.roots
      d <- children(graph, r)
    } {
      val moduleId = d.id
      val name     = moduleId.name
      p.println()
        .print("<tr><td>")
        .print(moduleId.organisation)
        .print("</td><td>")
        .print(name)
        .print("</td><td>")
        .print(
          s"""<a href="https://mvnrepository.com/artifact/${moduleId.organisation}/$name/${moduleId.version}" target="_blank">"""
        )
        .print(moduleId.version)
        .print("</a></td>")
      if (showLicenses) d.license.foreach(l => p.print("<td>").print(l).print("</td>"))
      p.print("</tr>")
    }
    p.indent(-2).println()
    p.print("</tbody>")
    p.indent(-2).println()
    p.print("</table></dd>").println()
  }

  private def renderTree(graph: ModuleGraph, p: Printer): Unit = {
    p.print("<dt>Dependency tree</dt><dd><pre>")
    for {
      r <- graph.roots
      d <- children(graph, r)
    } {
      renderTreeNode(p, graph, d)
    }
    p.print("</pre></dd>").println()
  }

  private def renderTreeNode(p: Printer, graph: ModuleGraph, n: Module): Unit =
    if (n.evictedByVersion.isEmpty) {
      val moduleId = n.id
      val name     = moduleId.name
      p.println()
        .print(moduleId.organisation)
        .print("    ")
        .print(name)
        .print(
          s"""    <a href="https://mvnrepository.com/artifact/${moduleId.organisation}/$name/${moduleId.version}" target="_blank">"""
        )
        .print(moduleId.version)
        .print("</a>")
      n.license.foreach(l => p.print("    ").print(l))
      if (children(graph, n).nonEmpty) {
        p.indent(4)
        children(graph, n).foreach(renderTreeNode(p, graph, _))
        p.indent(-4)
      }
    }

  private def children(graph: ModuleGraph, module: Module) = graph.dependencyMap(module.id)


}
