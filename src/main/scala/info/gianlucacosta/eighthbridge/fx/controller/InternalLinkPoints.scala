package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import scalafx.geometry.Point2D

trait InternalLinkPoints extends GraphCanvasController {
  override def createLinkInternalPoint(link: L, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] = {
    val updatedLink =
      link.visualCopy(internalPoints = updatedInternalPoints)

    Some(
      graph.replaceLink(link, updatedLink)
    )
  }

  override def canDragLinkInternalPoint(link: L, updatedInternalPoints: List[Point2D], originalInternalPoint: Point2D, updatedInternalPoint: Point2D): Boolean =
    true

  override def deleteLinkInternalPoint(link: L, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] = {
    val updatedLink =
      link.visualCopy(internalPoints = updatedInternalPoints)

    Some(
      graph.replaceLink(link, updatedLink)
    )
  }
}
