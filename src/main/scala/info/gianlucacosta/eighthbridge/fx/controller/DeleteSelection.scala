package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphCanvasController}
import scalafx.geometry.Point2D

trait DeleteSelection extends GraphCanvasController {
  override def deleteSelection(): Option[AnyVisualGraph] =
    Some(
      graph
        .removeLinks(graph.selectedLinks)
        .removeVertexes(graph.selectedVertexes)
    )
}
