package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.DirectedGraph
import scalafx.geometry.Point2D
import scalafx.Includes._
import info.gianlucacosta.helios.fx.Includes._

/**
  * Graph dedicated to rendering within GraphCanvas.
  */
trait VisualGraph[V <: VisualVertex, A <: VisualArc] extends DirectedGraph[V, A] {
  @transient
  lazy val selectedVertexes: Set[V] =
    vertexes.filter(vertex => vertex.selected)


  @transient
  lazy val selectedArcs: Set[A] =
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
                    selectionVertexes: Set[V] = Set(),
                    selectionArcs: Set[A] = Set()
                  ): this.type = {
    val updatedVertexes: Set[V] =
      vertexes.map(vertex =>
        vertex.setSelected(
          selectionVertexes.contains(vertex)
        )
      )

    val updatedArcs: Set[A] =
      arcs.map(arc =>
        arc.setSelected(
          selectionArcs.contains(arc)
        )
      )

    this
      .updateVertexes(updatedVertexes)
      .updateArcs(updatedArcs)
  }

  def moveSelectedVertexesBy(delta: Point2D): this.type = {
    val movedVertexes: Set[V] =
      selectedVertexes.map(vertex =>
        vertex.setCenter(
          vertex.center + delta
        )
      )

    updateVertexes(movedVertexes)
  }
}
