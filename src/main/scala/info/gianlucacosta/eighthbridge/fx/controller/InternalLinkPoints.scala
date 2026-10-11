package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import scalafx.geometry.Point2D

trait InternalLinkPoints extends GraphCanvasController {
  override def createLinkInternalPoint(link: VisualLink, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[AnyVisualGraph] = {
    Some(
      graph.updateLink(
        link.setInternalPoints(updatedInternalPoints)
      )
    )
  }

  override def canDragLinkInternalPoint(link: VisualLink, updatedInternalPoints: List[Point2D], originalInternalPoint: Point2D, updatedInternalPoint: Point2D): Boolean =
    true

  override def deleteLinkInternalPoint(link: VisualLink, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[AnyVisualGraph] = {
    Some(
      graph.updateLink(
        link.setInternalPoints(updatedInternalPoints)
      )
    )
  }
}
