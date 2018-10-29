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
    printer.println()
    val classes = Seq("dependencies", node.attributes.classesString).filter(_.nonEmpty)
    printer.print(s"""<dl class="${classes.mkString(" ")}">""")
    if (tree.roots.flatMap(_.children).nonEmpty) {
      renderDirect(node, tree.roots, printer)
      renderTree(node, tree.roots, printer)
    } else {
      printer.print("<dt>Direct dependencies</dt><dd>This module has no dependencies.</dd>")
    }
    printer.print("</dl>")
    printer.println()
  }

  private def renderDirect(node: DirectiveNode, roots: Seq[ModuleTreeNode], p: Printer): Unit = {
    p.print("<dt>Direct dependencies</dt><dd><table>")
    p.indent(2).println()
    p.print("<thead><tr><th>Organization</th><th>Artifact</th><th>Version</th><th>License</th></tr></thead>").println()
    p.print("<tbody>")
    p.indent(2)
    for {
      r <- roots
      d <- r.children
    } {
      val moduleId = d.node.id
      val name     = moduleId.name
      p.println()
        .print("<tr><td>")
        .print(moduleId.organisation)
        .print("</td><td>")
        .print(name)
        .print("</td><td>")
        .print(s"""<a href="https://mvnrepository.com/artifact/${moduleId.organisation}/$name/${moduleId.version}" target="_blank">""")
        .print(moduleId.version)
        .print("</a></td>")
      d.node.license.foreach(l => p.print("<td>").print(l).print("</td>"))
      p.print("</tr>")
    }
    p.indent(-2).println()
    p.print("</tbody>")
    p.indent(-2).println()
    p.print("</table></dd>").println()
  }

  private def renderTree(node: DirectiveNode, roots: Seq[ModuleTreeNode], p: Printer): Unit = {
    p.print("<dt>Dependency tree</dt><dd><pre>")
    p.println()
    for {
      r <- roots
      d <- r.children
    } {
      renderTreeNode(p, d)
    }
    p.print("</pre></dd>").println()
  }

  private def renderTreeNode(p: Printer, n: ModuleTreeNode): Unit =
    if (n.node.evictedByVersion.isEmpty) {
      val moduleId = n.node.id
      val name     = moduleId.name
      p.print(moduleId.organisation)
        .print("    ")
        .print(name)
        .print(
          s"""    <a href="https://mvnrepository.com/artifact/${moduleId.organisation}/$name/${moduleId.version}" target="_blank">""")
        .print(moduleId.version)
        .print("</a>")
      n.node.license.foreach(l => p.print("    ").print(l))
      if (n.children.nonEmpty) {
        p.indent(4)
        p.println()
        n.children.foreach(renderTreeNode(p, _))
        p.indent(-4)
      }
      p.println()
    }

}
