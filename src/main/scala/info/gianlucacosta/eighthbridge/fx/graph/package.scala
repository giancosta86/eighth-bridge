package info.gianlucacosta.eighthbridge.fx

//import info.gianlucacosta.helios.fx.Includes._
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.helios.fx.Includes.extendPoint2D

package object graph {
  type VisualGraph =
    DirectedGraph[VisualVertex, VisualLink]

  implicit class VisualGraphExtensions(self: VisualGraph) {
    def selectedVertexes: Set[VisualVertex] =
      self.vertexes.filter(vertex => vertex.selected)


    def selectedLinks: Set[VisualLink] =
      self.links.filter(link => link.selected)


    def selectionEmpty: Boolean =
      self.selectedVertexes.isEmpty && selectedLinks.isEmpty


    def selectAll: VisualGraph =
      self.setSelection(
        self.vertexes,
        self.links
      )

    def deselectAll: VisualGraph =
      self.setSelection(Set(), Set())


    def setSelection(
                      selectedVertexes: Set[VisualVertex],
                      selectedLinks: Set[VisualLink]
                    ): VisualGraph = {
      val updatedVertexes: Set[VisualVertex] =
        self.vertexes.map(vertex =>
          vertex.setSelected(
            selectedVertexes.contains(vertex)
          )
        )

      val updatedLinks: Set[VisualLink] =
        self.links.map(link =>
          link.setSelected(
            selectedLinks.contains(link)
          )
        )

      self
        .updateVertexes(updatedVertexes)
        .updateLinks(updatedLinks)
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
