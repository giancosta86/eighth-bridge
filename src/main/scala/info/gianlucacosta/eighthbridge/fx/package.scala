package info.gianlucacosta.eighthbridge

import info.gianlucacosta.eighthbridge.fx.Delta2D._
import info.gianlucacosta.eighthbridge.graphs.directed.DirectedGraph
import info.gianlucacosta.eighthbridge.graphs.facets.{HasCenterSettable, HasSelected, HasSelectedSettable}
import info.gianlucacosta.eighthbridge.graphs.undirected.UndirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{Facet, Graph}
import info.gianlucacosta.helios.fx.Includes.extendPoint2D

package object fx {
  type VisualGraph =
    Graph[VisualVertex, VisualLink]

  type VisualUndirectedGraph =
    UndirectedGraph[VisualVertex, VisualEdge]

  type VisualDirectedGraph =
    DirectedGraph[VisualVertex, VisualArc]

  implicit class VisualGraphExtensions(self: VisualGraph) {
    def selectedVertexes: Set[VisualVertex] =
      self.vertexes.filter(vertex =>
        Facet.as[HasSelected](vertex)
          .exists(_.selected)
      )

    def selectedLinks: Set[VisualLink] =
      self.links.filter(link =>
        Facet.as[HasSelected](link)
          .exists(_.selected)
      )

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
          vertex
            .asInstanceOf[VisualVertex with HasSelectedSettable]
            .setSelected(
              selectedVertexes.contains(vertex)
            )
        )

      val updatedLinks: Set[VisualLink] =
        self.links.map(link =>
          link
            .asInstanceOf[VisualLink with HasSelectedSettable]
            .setSelected(
            selectedLinks.contains(link)
          )
        )

      self
        .updateVertexes(updatedVertexes)
        .updateLinks(updatedLinks)
    }

    def moveSelectedVertexesBy(delta: Delta2D): VisualGraph = {
      val movedVertexes: Set[VisualVertex] =
        self.selectedVertexes.map(vertex =>
          vertex
            .asInstanceOf[VisualVertex with HasCenterSettable]
            .setCenter(
            (vertex.center + delta).nonNegative
          )
        )

      self.updateVertexes(movedVertexes)
    }
  }
}
