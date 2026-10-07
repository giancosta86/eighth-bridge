package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphCanvasController, VisualGraph, VisualLink, VisualVertex}
import scalafx.geometry.Point2D

trait DeleteSelection[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]] extends GraphCanvasController[V, L, G] {
  override def deleteSelection(): Option[G] = {
    val selectedVertexes =
      graph.selectedVertexes

    val selectedLinks =
      graph.selectedLinks

    Some(
      graph
        .removeLinks(selectedLinks)
        .removeVertexes(selectedVertexes)
    )
  }
}
