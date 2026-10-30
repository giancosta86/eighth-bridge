package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.arcnodes.labeled.LabeledLinkNode
import info.gianlucacosta.eighthbridge.fx.arcnodes.plain.PlainLinkNode
import info.gianlucacosta.eighthbridge.fx.graph.{ArcBarbs, LinkHandle, Delta, LabeledVisualLink, VisualLink, VisualGraph, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled.LabeledVertexNode
import info.gianlucacosta.eighthbridge.fx.vertexnodes.plain.PlainVertexNode
import info.gianlucacosta.eighthbridge.graphs.features.{Labeled, Named}
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

  private[fx] final def graphCanvas_=(graphCanvas: GraphCanvas): Unit = {
    require(
      _graphCanvas == null,
      "The controller must not be already associated with another GraphCanvas!"
    )

    _graphCanvas = graphCanvas
  }

  protected final def graph: VisualGraph =
    graphCanvas.graph()

  protected final def graph_=(graph: VisualGraph): Unit = {
    graphCanvas.graph() =
      graph
  }

  def createBackgroundNode(): BackgroundNode =
    new DefaultBackgroundNode(graphCanvas)

  def addVertex(center: Point2D): Unit = {}

  def addLink(sourceVertex: VisualVertex, targetVertex: VisualVertex): Unit = {}

  def createVertexNode(vertex: VisualVertex): VertexNode = ???

  def createLinkNode(link: VisualLink): LinkNode = ???

  def editVertex(vertex: VisualVertex): Unit = {}

  def editLink(link: VisualLink): Unit = {}

  def canDrawSelectingArea: Boolean =
    false

  def setSelection(selectedVertexes: Set[VisualVertex], selectedArcs: Set[VisualLink]): Unit = {}

  def dragLinkLabel(link: LabeledVisualLink, actualCenter: Point2D, delta: Delta): Unit = {}

  def resetLinkLabelCenter(link: LabeledVisualLink): Unit = {}

  def minCanvasDimension: Dimension2D =
    new Dimension2D(
      800,
      600
    )

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
            vertexNode.centerX() + vertexNode.width() / 2
          )
          .max

      val maxBottomEdge =
        graphCanvas
          .vertexNodesByVertex
          .values
          .map(vertexNode =>
            vertexNode.centerY() + vertexNode.height() / 2
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



  def deleteSelection(): Unit = {}

  def dragSelectedVertexes(delta: Delta): Unit = {}

  def addLinkHandle(link: VisualLink, index: Int, point: Point2D): Unit = {}

  def deleteLinkHandle(link: VisualLink, handle: LinkHandle): Unit = {}

  def dragLinkHandle(link: VisualLink, linkHandle: LinkHandle, delta: Delta): Unit = {}
}
