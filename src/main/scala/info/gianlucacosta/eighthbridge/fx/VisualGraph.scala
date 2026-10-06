package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.graphs.point2point.DirectedGraph

/**
  * Graph dedicated to rendering; it is especially useful in combination with GraphCanvas.
  *
  * Since such a graph is designed to be interactively drawn by users, it is necessarily
  * based on arc bindings - therefore, it's up to the renderer to choose whether to draw it
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
    var updatedVertexes: Set[V] = vertexes.map(vertex =>
      vertex.visualCopy(selected = selectionVertexes.contains(vertex))
    )

    var updatedLinks: Set[L] = links.map(link =>
      link.visualCopy(selected = selectionLinks.contains(link))
    )

    replaceVertexes(updatedVertexes)
      .replaceLinks(updatedLinks)
  }
}
