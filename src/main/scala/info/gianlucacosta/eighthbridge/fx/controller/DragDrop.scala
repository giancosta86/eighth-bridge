package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphCanvasController}
import scalafx.geometry.Point2D
import info.gianlucacosta.helios.fx.Includes._

/**
  * Controller mixin enabling both selection and drag&drop.
  */
trait DragDrop extends GraphCanvasController {
  override def dragSelection(delta: Point2D): Option[AnyVisualGraph] = {
    Some(
      graph.mapVertexes(vertex =>
        if (vertex.selected)
          vertex .visualCopy(center = (vertex.center + delta).clip(graphCanvas.dimension))
        else
          vertex
      )
    )
  }

  override def dragLinkLabel(link: VisualLink, originalCenter: Point2D, updatedCenter: Point2D): Option[AnyVisualGraph] =
    Some(
      graph.updateLink(
        link.setLabelCenter(Some(
          updatedCenter
        ))
      )
    )
}
