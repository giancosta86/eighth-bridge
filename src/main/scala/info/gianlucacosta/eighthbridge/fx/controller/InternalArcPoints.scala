package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import scalafx.geometry.Point2D

trait InternalArcPoints extends GraphCanvasController {
  override def createArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[AnyVisualGraph] = {
    Some(
      graph.updateArc(
        arc.setInternalPoints(updatedInternalPoints)
      )
    )
  }

  override def canDragArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], originalInternalPoint: Point2D, updatedInternalPoint: Point2D): Boolean =
    true

  override def deleteArcInternalPoint(arc: VisualArc, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[AnyVisualGraph] = {
    Some(
      graph.updateArc(
        arc.setInternalPoints(updatedInternalPoints)
      )
    )
  }
}
