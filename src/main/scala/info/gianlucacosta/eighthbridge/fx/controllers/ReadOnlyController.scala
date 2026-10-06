package info.gianlucacosta.eighthbridge.fx.controllers

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, GraphCanvasController, VisualGraph, VisualLink, VisualVertex}
import scalafx.geometry.Point2D

/**
  * Controller only showing a graph - totally preventing interactivity
  */
class ReadOnlyController[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]](val renderDirected: Boolean) extends GraphCanvasController[V, L, G] {
  override def canDrawSelectionRectangle: Boolean =
    false

  override def createVertex(graph: G, center: Point2D): Option[G] =
    None

  override def createLink(graph: G, sourceVertex: V, targetVertex: V): Option[G] =
    None

  override def setLinkSelectedState(graph: G, link: L, selected: Boolean): Option[G] =
    None

  override def editVertex(graph: G, vertex: V): Option[G] =
    None

  override def setSelection(graph: G, selectionVertexes: Set[V], selectionLinks: Set[L]): Option[G] =
    None

  override def editLink(graph: G, link: L): Option[G] =
    None

  override def setVertexSelectedState(graph: G, vertex: V, selected: Boolean): Option[G] =
    None

  override def deleteSelection(graphCanvas: GraphCanvas[V, L, G], graph: G): Option[G] =
    None

  override def dragSelection(graphCanvas: GraphCanvas[V, L, G], delta: Point2D): Option[G] =
    None

  override def createLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] =
    None

  override def deleteLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] =
    None

  override def canDragLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], oldInternalPoint: Point2D, newInternalPoint: Point2D): Boolean =
    false

  override def dragLinkLabel(graph: G, link: L, oldCenter: Point2D, newCenter: Point2D): Option[G] =
    None


}
