package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{ArcNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.fx.graph.VisualArc
import scalafx.Includes._

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

  arcBarbs.onChange(updateBarbSegments())

  private var barbSegments: Option[BarbSegments] =
    None

  private def updateBarbSegments(): Unit = {
    if (arcBarbs().isDefined && barbSegments.isEmpty) {
      val createdBarbSegments =
        new BarbSegments(sourceVertexNode, targetVertexNode, arc.barbs.get)

      barbSegments =
        Some(createdBarbSegments)

      children.add(createdBarbSegments)
    } else if (arcBarbs().isEmpty) {
      barbSegments.foreach(children.remove(_))

      barbSegments =
        None
    }
  }
}
