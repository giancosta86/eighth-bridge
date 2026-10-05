package info.gianlucacosta.eighthbridge.fx.canvas.basic

import info.gianlucacosta.eighthbridge.fx.canvas.GraphCanvas
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualGraph

import scalafx.geometry.Point2D

/**
  * Controller only showing a graph - totally preventing interactivity
  */
class ReadOnlyController[V <: BasicVertex, L <: BasicLink, G <: VisualGraph[V, L]](val renderDirected: Boolean) extends BasicController[V, L, G] {
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
