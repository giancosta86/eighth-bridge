package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.helios.fx.Includes._
import scalafx.geometry.{Dimension2D, Point2D}

/**
  * Controller providing behavior for GraphCanvas
  */
abstract class GraphCanvasController[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] {
  private var _graphCanvas: GraphCanvas[V, L, G] = _

  protected[fx] final def graphCanvas: GraphCanvas[V, L, G] = _graphCanvas

  protected[fx] final def graphCanvas_=(graphCanvas: GraphCanvas[V, L, G]): Unit = {
    _graphCanvas = graphCanvas
  }

  protected final def graph: G = graphCanvas.graph

  def renderDirected: Boolean

  def createBackgroundNode(): BackgroundNode[V, L, G] =
    new DefaultBackgroundNode(graphCanvas)

  //TODO! Should I remove the vertex here?
  def createVertexNode(vertex: V): VertexNode[V, L, G] =
    new DefaultVertexNode(graphCanvas)

  //TODO! Should I remove the link here?
  def createLinkNode(sourceVertex: V, targetVertex: V, link: L): LinkNode[V, L, G] =
    new DefaultLinkNode(graphCanvas, sourceVertex.id, targetVertex.id)

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
    if (graphCanvas.vertexNodes.isEmpty)
      minCanvasDimension
    else {
      val maxRightEdge =
        graphCanvas
          .vertexNodes
          .values
          .map(vertexNode =>
            vertexNode.vertex.center.x + vertexNode.width() / 2
          )
          .max

      val maxBottomEdge =
        graphCanvas
          .vertexNodes
          .values
          .map(vertexNode =>
            vertexNode.vertex.center.y + vertexNode.height() / 2
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
