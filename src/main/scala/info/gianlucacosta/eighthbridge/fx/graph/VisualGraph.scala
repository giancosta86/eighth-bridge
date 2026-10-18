package info.gianlucacosta.eighthbridge.fx.graph

import Delta._
import info.gianlucacosta.eighthbridge.theory.DirectedGraph


/**
  * Graph dedicated to rendering within GraphCanvas.
  */
class VisualGraph(
                                                      val vertexes: Set[VisualVertex] =
                                                        Set[VisualVertex](),
                                                      protected val arcsByVertexPair: Map[(VisualVertex, VisualVertex), Set[VisualArc]] =
                                                        Map[(VisualVertex, VisualVertex), Set[VisualArc]]()
) extends DirectedGraph[VisualVertex, VisualArc] {
  override protected def graphCopy(vertexes: Set[VisualVertex], arcsByVertexPair: Map[(VisualVertex, VisualVertex), Set[VisualArc]]): this.type =
    new VisualGraph(
      vertexes = vertexes,

      arcsByVertexPair = arcsByVertexPair
    ).asInstanceOf[this.type]

  override def equals(obj: Any): Boolean =
    false

  override def hashCode(): Int =
    0

  @transient
  lazy val selectedVertexes: Set[VisualVertex] =
    vertexes.filter(vertex => vertex.selected)


  @transient
  lazy val selectedArcs: Set[VisualArc] =
    arcs.filter(arc => arc.selected)


  @transient
  lazy val selectionEmpty: Boolean =
    selectedVertexes.isEmpty && selectedArcs.isEmpty


  @transient
  lazy val selectAll: this.type =
    setSelection(vertexes, arcs)


  @transient
  lazy val deselectAll: this.type =
    setSelection(Set(), Set())


  def setSelection(
                    selectionVertexes: Set[VisualVertex] = Set(),
                    selectionArcs: Set[VisualArc] = Set()
                  ): this.type = {
    val updatedVertexes: Set[VisualVertex] =
      vertexes.map(vertex =>
        vertex.setSelected(
          selectionVertexes.contains(vertex)
        )
      )

    val updatedArcs: Set[VisualArc] =
      arcs.map(arc =>
        arc.setSelected(
          selectionArcs.contains(arc)
        )
      )

    this
      .updateVertexes(updatedVertexes)
      .updateArcs(updatedArcs)
  }

  def moveSelectedVertexesBy(delta: Delta): this.type = {
    val movedVertexes: Set[VisualVertex] =
      selectedVertexes.map(vertex =>
        vertex.setCenter(
          vertex.center + delta
        )
      )

    updateVertexes(movedVertexes)
  }
}
