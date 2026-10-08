package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphCanvasController, VisualGraph, VisualLink, VisualVertex}
import scalafx.geometry.Point2D
import info.gianlucacosta.helios.fx.Includes._

/**
  * Controller mixin enabling both selection and drag&drop.
  */
trait DragDrop[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]] extends GraphCanvasController[V, L, G] {
  override def dragSelection(delta: Point2D): Option[G] = {
    Some(
      graph.replaceVertexes(
        graph.selectedVertexes.map(vertex => {
          val newCenter =
            (vertex.center + delta).clip(graphCanvas.dimension)

          vertex.visualCopy(center = newCenter)
        })
      )
    )
  }

  override def dragLinkLabel(link: L, originalCenter: Point2D, updatedCenter: Point2D): Option[G] = {
    val newLink = link.visualCopy(
      labelCenter = Some(
        updatedCenter
      )
    )

    Some(
      graph.replaceLink(newLink)
    )
  }
}
