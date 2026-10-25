package info.gianlucacosta.eighthbridge.fx

//import info.gianlucacosta.helios.fx.Includes._
import scalafx.Includes._
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.helios.fx.Includes.extendPoint2D

package object graph {
  type VisualGraph = DirectedGraph[VisualVertex, VisualLink]

  implicit class VisualGraphExtensions(self: VisualGraph) {
    def selectedVertexes: Set[VisualVertex] =
      self.vertexes.filter(vertex => vertex.selected)


    def selectedArcs: Set[VisualLink] =
      self.arcs.filter(arc => arc.selected)


    def selectionEmpty: Boolean =
      self.selectedVertexes.isEmpty && selectedArcs.isEmpty


    def selectAll: VisualGraph =
      self.setSelection(
        self.vertexes,
        self.arcs
      )


    def deselectAll: VisualGraph =
      self.setSelection(Set(), Set())


    def setSelection(
                      selectedVertexes: Set[VisualVertex],
                      selectedArcs: Set[VisualLink]
                    ): VisualGraph = {
      val updatedVertexes: Set[VisualVertex] =
        self.vertexes.map(vertex =>
          vertex.setSelected(
            selectedVertexes.contains(vertex)
          )
        )

      val updatedArcs: Set[VisualLink] =
        self.arcs.map(arc =>
          arc.setSelected(
            selectedArcs.contains(arc)
          )
        )

      self
        .updateVertexes(updatedVertexes)
        .updateArcs(updatedArcs)
    }

    def moveSelectedVertexesBy(delta: Delta): VisualGraph = {
      val movedVertexes: Set[VisualVertex] =
        self.selectedVertexes.map(vertex =>
          vertex.setCenter(
            (vertex.center + delta).nonNegative
          )
        )

      self.updateVertexes(movedVertexes)
    }
  }
}
