package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.Includes._
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.Includes._

/**
  * Controller providing behavior for GraphCanvas
  */
abstract class GraphCanvasController {
  private var _graphCanvas: GraphCanvas = _

  protected[fx] final def graphCanvas: GraphCanvas =
    _graphCanvas

  protected[fx] final def graphCanvas_=(graphCanvas: GraphCanvas): Unit = {
    _graphCanvas = graphCanvas
  }

  protected final def graph: AnyVisualGraph =
    graphCanvas.graph()

  //TODO! Turn this into a function of VisualLink, that the 2 specific trait will implement by returning their related constants!
  def renderDirected: Boolean

  def createBackgroundNode(): BackgroundNode =
    new DefaultBackgroundNode(graphCanvas)

  def createVertexNode(vertex: VisualVertex): VertexNode =
    new DefaultVertexNode(graphCanvas, vertex)

  def createLinkNode(link: VisualLink): LinkNode =
    new DefaultLinkNode(graphCanvas, link)

  def deleteSelection(): Option[AnyVisualGraph] =
    None

  def createVertex(center: Point2D): Option[AnyVisualGraph] =
    None

  def createLink(sourceVertex: VisualVertex, targetVertex: VisualVertex): Option[AnyVisualGraph] =
    None

  def editVertex(vertex: VisualVertex): Option[AnyVisualGraph] =
    None

  def editLink(link: VisualLink): Option[AnyVisualGraph] =
    None

  def canDrawSelectionRectangle: Boolean =
    false

  def setVertexSelectedState(vertex: VisualVertex, selected: Boolean): Option[AnyVisualGraph] =
    None

  def setLinkSelectedState(link: VisualLink, selected: Boolean): Option[AnyVisualGraph] =
    None

  def setSelection(selectionVertexes: Set[VisualVertex], selectionLinks: Set[VisualLink]): Option[AnyVisualGraph] =
    None

  //TODO! Delta should have a dedicated type!
  def dragSelection(delta: Point2D): Option[AnyVisualGraph] =
    None

  def createLinkInternalPoint(link: VisualLink, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[AnyVisualGraph] =
    None

  def canDragLinkInternalPoint(link: VisualLink, updatedInternalPoints: List[Point2D], originalInternalPoint: Point2D, updatedInternalPoint: Point2D): Boolean =
    false

  def deleteLinkInternalPoint(link: VisualLink, updatedInternalPoints: List[Point2D], internalPoint: Point2D): Option[AnyVisualGraph] =
    None

  def dragLinkLabel(link: VisualLink, originalCenter: Point2D, updatedCenter: Point2D): Option[AnyVisualGraph] =
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
