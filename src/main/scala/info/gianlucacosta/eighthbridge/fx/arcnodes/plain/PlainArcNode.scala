package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{ArcNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.fx.graph.VisualArc
import scalafx.scene.shape.Line

class PlainArcNode(graphCanvas: GraphCanvas, arc: VisualArc)
  extends ArcNode(graphCanvas, arc) {

  private val (sourceVertex, targetVertex) =
    graph.getAnchors(arc).get

  private val sourceVertexNode =
    graphCanvas.vertexNodesByVertex(sourceVertex)

  private val targetVertexNode =
    graphCanvas.vertexNodesByVertex(targetVertex)

  private val line =
    new LinkLine(sourceVertexNode, targetVertexNode)

  children.add(line)
}
