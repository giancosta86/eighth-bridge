package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{Delta, LabeledVisualLink, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.linknodes.plain.PlainLinkNode
import info.gianlucacosta.eighthbridge.fx.vertexnodes.plain.PlainVertexNode
import info.gianlucacosta.eighthbridge.graphs.facets.{HasOptionalCustomLabelCenterSettable, LinkHandle}
import scalafx.Includes._
import scalafx.geometry.{Dimension2D, Point2D}

/**
  * Controller providing behavior for GraphCanvas
  */
class GraphCanvasController {
  private var _graphCanvas: GraphCanvas =
    _

  protected[fx] final def graphCanvas: GraphCanvas =
    _graphCanvas

  private[fx] def graphCanvas_=(graphCanvas: GraphCanvas): Unit = {
    _graphCanvas =
      graphCanvas
  }

  protected final def graph: VisualGraph =
    graphCanvas.graph()

  protected final def graph_=(graph: VisualGraph): Unit = {
    graphCanvas.graph() =
      graph
  }

  def createBackgroundNode(): BackgroundNode =
    new DefaultBackgroundNode(graphCanvas)

  def createVertexNode(vertex: VisualVertex): VertexNode =
    new PlainVertexNode(graphCanvas, vertex)

  def createLinkNode(link: VisualLink): LinkNode =
    new PlainLinkNode(graphCanvas, link)

  def addVertex(center: Point2D): Unit = {}

  def addLink(sourceVertex: VisualVertex, targetVertex: VisualVertex): Unit = {}

  def editVertex(vertex: VisualVertex): Unit = {}

  def editLink(link: VisualLink): Unit = {}

  def canDrawSelectingArea: Boolean =
    false

  def setSelection(selectedVertexes: Set[VisualVertex], selectedLinks: Set[VisualLink]): Unit = {}

  def deleteSelection(): Unit = {}

  def dragSelectedVertexes(delta: Delta): Unit = {}

  def addLinkHandle(link: VisualLink, index: Int, point: Point2D): Unit = {}

  def deleteLinkHandle(link: VisualLink, handle: LinkHandle): Unit = {}

  def dragLinkHandle(link: VisualLink, linkHandle: LinkHandle, delta: Delta): Unit = {}

  def dragLinkLabel(link: VisualLink with HasOptionalCustomLabelCenterSettable, actualCenter: Point2D, delta: Delta): Unit = {}

  def resetLinkLabelCenter(link: VisualLink with HasOptionalCustomLabelCenterSettable): Unit = {}

  def minCanvasDimension: Dimension2D =
    new Dimension2D(
      800,
      600
    )

  //TODO! Move the margin and the min size to GraphCanvas
  def graphMargin: Int =
    20

  //TODO! Handle the strange glitch occurring when a vertex has centerX == 0
  def canvasDimension: Dimension2D = {
    if (graphCanvas.vertexNodesByVertex.isEmpty)
      minCanvasDimension
    else {
      val maxRightEdge =
        graphCanvas
          .vertexNodesByVertex
          .values
          .map(vertexNode =>
            vertexNode.vertex().center.x + vertexNode.width() / 2
          )
          .max

      val maxBottomEdge =
        graphCanvas
          .vertexNodesByVertex
          .values
          .map(vertexNode =>
            vertexNode.vertex().center.y + vertexNode.height() / 2
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
