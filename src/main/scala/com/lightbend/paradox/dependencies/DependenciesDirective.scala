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
import org.pegdown.ast.{DirectiveNode, TextNode, Visitor}

class DependenciesDirective(nameToDependencies: String => ModuleTree) extends LeafBlockDirective("dependencies") {
  def render(node: DirectiveNode, visitor: Visitor, printer: Printer): Unit = {
    val moduleName = node.attributes.value("module")
    printer.println.print(nameToDependencies(moduleName) /*.roots.map { n =>
      n.mkString("\n")
    }*/ .toString)
  }

  private def renderNode(indent: String, n: ModuleTreeNode): Unit = {}

}
