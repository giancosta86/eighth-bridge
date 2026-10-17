package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

trait InternalArcPoints extends GraphCanvasController {
  /*
  override def createArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[VisualGraph] = {
    Some(
      graph.updateArc(
        arc.setInternalPoints(updatedInternalPoints)
      )
    )
  }

  override def canDragArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], originalInternalPoint: Point2D, updatedInternalPoint: Point2D): Boolean =
    true

  override def deleteArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[VisualGraph] = {
    Some(
      graph.updateArc(
        arc.setInternalPoints(updatedInternalPoints)
      )
    )
  }
   */
}
