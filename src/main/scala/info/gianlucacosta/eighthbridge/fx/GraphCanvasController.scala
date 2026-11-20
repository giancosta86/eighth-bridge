package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.linknodes.plain.PlainLinkNode
import info.gianlucacosta.eighthbridge.fx.vertexnodes.plain.PlainVertexNode
import info.gianlucacosta.eighthbridge.core.facets.LinkHandle
import scalafx.Includes._
import scalafx.geometry.Point2D

/**
 * Controller providing behavior for GraphCanvas
 */
class GraphCanvasController {
  private var _graphCanvas: GraphCanvas = _

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

  def editLink(link: VisualLink): Unit = {
    println("EDITING THE LINK!")
  }

  def canDrawSelectingArea: Boolean =
    false

  def setSelection(selectedVertexes: Set[VisualVertex], selectedLinks: Set[VisualLink]): Unit = {}

  def deleteSelection(): Unit = {}

  def dragSelectedVertexes(delta: Delta2D): Unit = {}

  def addLinkHandle(link: VisualLink, index: Int, point: Point2D): Unit = {}

  def deleteLinkHandle(link: VisualLink, handle: LinkHandle): Unit = {}

  def dragLinkHandle(link: VisualLink, linkHandle: LinkHandle, delta: Delta2D): Unit = {}

  def dragLinkLabel(link: VisualLink, currentCenter: Point2D, delta: Delta2D): Unit = {}

  def resetLinkLabelCenter(link: VisualLink): Unit = {}
}
