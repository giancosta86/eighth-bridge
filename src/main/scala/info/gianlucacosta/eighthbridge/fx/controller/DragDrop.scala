package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphCanvasController}
import scalafx.geometry.Point2D
import info.gianlucacosta.helios.fx.Includes._

/**
  * Controller mixin enabling both selection and drag&drop.
  */
trait DragDrop extends GraphCanvasController {
  override def dragSelectedVertexesBy(delta: Delta): Option[VisualGraph] =
    Some(
      graph.moveSelectedVertexesBy(delta)
    )

  /*
  override def dragArcLabel(arc: VisualArc, originalCenter: Point2D, updatedCenter: Point2D): Option[VisualGraph] = {

    Some(
      graph.updateArc(
        arc.setLabelCenter(Some(
          updatedCenter
        ))
      )
    )    None
  }
   */
}
