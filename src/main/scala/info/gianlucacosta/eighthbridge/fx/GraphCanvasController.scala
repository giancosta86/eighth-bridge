package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.Includes._
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.Includes._

/**
  * Controller providing behavior for GraphCanvas
  */
abstract class GraphCanvasController {
  private var _graphCanvas: GraphCanvas = _

  protected[fx] final def graphCanvas: GraphCanvas = _graphCanvas

  protected[fx] final def graphCanvas_=(graphCanvas: GraphCanvas): Unit = {
    _graphCanvas = graphCanvas
  }

  protected final def graph: G = graphCanvas.graph()

  def renderDirected: Boolean

  def createBackgroundNode(): BackgroundNode =
    new DefaultBackgroundNode(graphCanvas)

  def createVertexNode(vertex: V): VertexNode =
    new DefaultVertexNode(graphCanvas, vertex)

  def createLinkNode(link: L): LinkNode =
    new DefaultLinkNode(graphCanvas, link)

  def deleteSelection(): Option[G] =
    None

  def createVertex(center: Point2D): Option[G] =
    None

  def createLink(sourceVertex: V, targetVertex: V): Option[G] =
    None

  def editVertex(vertex: V): Option[G] =
    None

  def editLink(link: L): Option[G] =
    None

  def canDrawSelectionRectangle: Boolean =
    false

  def setVertexSelectedState(vertex: V, selected: Boolean): Option[G] =
    None

  def setLinkSelectedState(link: L, selected: Boolean): Option[G] =
    None

  def setSelection(selectionVertexes: Set[V], selectionLinks: Set[L]): Option[G] =
    None

  //TODO! Delta should have a dedicated type!
  def dragSelection(delta: Point2D): Option[G] =
    None

  def createLinkInternalPoint(link: L, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] =
    None

  def canDragLinkInternalPoint(link: L, updatedInternalPoints: List[Point2D], originalInternalPoint: Point2D, updatedInternalPoint: Point2D): Boolean =
    false

  def deleteLinkInternalPoint(link: L, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] =
    None

  def dragLinkLabel(link: L, originalCenter: Point2D, updatedCenter: Point2D): Option[G] =
    None

  def minCanvasDimension: Dimension2D =
    new Dimension2D(
      800,
      600
    )

  def graphMargin: Int =
    20

  def canvasDimension: Dimension2D = {
    if (graphCanvas.vertexNodesByVertex.isEmpty)
      minCanvasDimension
    else {
      val maxRightEdge =
        graphCanvas
          .vertexNodesByVertex
          .values
          .map(vertexNode =>
            vertexNode.vertexInGraph.center.x + vertexNode.width() / 2
          )
          .max

      val maxBottomEdge =
        graphCanvas
          .vertexNodesByVertex
          .values
          .map(vertexNode =>
            vertexNode.vertexInGraph.center.y + vertexNode.height() / 2
          )
          .max


      new Dimension2D(
        math.max(
          maxRightEdge + graphMargin,
          minCanvasDimension.width
        ),

        math.max(
          maxBottomEdge + graphMargin,
          minCanvasDimension.height
        )
      )
    }
  }
}
