package info.gianlucacosta.eighthbridge.fx.graph

import Delta._
import info.gianlucacosta.eighthbridge.graphs.DirectedGraph
import info.gianlucacosta.helios.fx.Includes.extendPoint2D
import scalafx.geometry.Point2D

import Math._

/**
  * Graph dedicated to rendering within GraphCanvas.
  */
class VisualGraph(
                                                      vertexes: Set[VisualVertex] =
                                                        Set[VisualVertex](),
                                                      arcsByVertexPair: Map[(VisualVertex, VisualVertex), Set[VisualArc]] =
                                                        Map[(VisualVertex, VisualVertex), Set[VisualArc]]()
) extends DirectedGraph[VisualVertex, VisualArc](vertexes, arcsByVertexPair) {
  override protected def graphCopy(updatedVertexes: Set[VisualVertex], updatedArcsByVertexPair: Map[(VisualVertex, VisualVertex), Set[VisualArc]]): this.type =
    new VisualGraph(
      vertexes =
        updatedVertexes,

      arcsByVertexPair =
        updatedArcsByVertexPair
    ).asInstanceOf[this.type]


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
                    selectedVertexes: Set[VisualVertex],
                    selectedArcs: Set[VisualArc]
                  ): this.type = {
    val updatedVertexes: Set[VisualVertex] =
      vertexes.map(vertex =>
        vertex.setSelected(
          selectedVertexes.contains(vertex)
        )
      )

    val updatedArcs: Set[VisualArc] =
      arcs.map(arc =>
        arc.setSelected(
          selectedArcs.contains(arc)
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
          (vertex.center + delta).nonNegative
        )
      )

    updateVertexes(movedVertexes)
  }
}
