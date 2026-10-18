package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphCanvasController}
import scalafx.geometry.Point2D

trait DeleteSelection extends GraphCanvasController {
  override def deleteSelection(): Option[VisualGraph] = {
    Some(
      graph
        .removeArcs(graph.selectedArcs)
        .removeVertexes(graph.selectedVertexes)
    )
  }
}
