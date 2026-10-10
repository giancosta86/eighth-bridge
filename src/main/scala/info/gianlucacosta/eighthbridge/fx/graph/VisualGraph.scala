package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.DirectedGraph

/**
  * Graph dedicated to rendering within GraphCanvas.
  *
  * Since such a graph is designed to be interactively drawn by users, it is necessarily
  * based on arc bindings - therefore, it's up to the controller to choose whether to draw it
  * with edges instead of arcs.
  */
trait VisualGraph[V <: VisualVertex, L <: VisualLink] extends DirectedGraph[V, L] {
  @transient
  lazy val selectedVertexes: Set[V] =
    vertexes.filter(vertex => vertex.selected)


  @transient
  lazy val selectedLinks: Set[L] =
    links.filter(link => link.selected)


  @transient
  lazy val selectAll: this.type =
    setSelection(vertexes, links)


  @transient
  lazy val deselectAll: this.type =
    setSelection(Set(), Set())


  @transient
  lazy val selectionEmpty: Boolean =
    selectedVertexes.isEmpty && selectedLinks.isEmpty


  def setSelection(selectionVertexes: Set[V] = Set(), selectionLinks: Set[L] = Set()): this.type = {
    val updatedVertexes: Set[V] =
      vertexes.map(vertex =>
        vertex.setSelected(selectionVertexes.contains(vertex))
      )

    val updatedLinks: Set[L] =
      links.map(link =>
        link.setSelected(selectionLinks.contains(link))
      )

    this
      .updateVertexes(updatedVertexes)
      .updateLinks(updatedLinks)
  }
}
