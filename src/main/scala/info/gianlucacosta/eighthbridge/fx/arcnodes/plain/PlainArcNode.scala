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

  private val shaft =
    new Shaft(sourceVertexNode, targetVertexNode)

  children.add(shaft)

  private var barbs: Option[Barbs] =
    None

  arcBarbs.onChange(updateBarbSegments())

  private def updateBarbSegments(): Unit = {
    if (arcBarbs().isDefined && barbs.isEmpty) {
      val brandNewBarbs =
        new Barbs(sourceVertexNode, targetVertexNode, arc.barbs.get)

      barbs =
        Some(brandNewBarbs)

      children.add(brandNewBarbs)
    } else if (arcBarbs().isEmpty) {
      barbs.foreach(children.remove(_))

      barbs =
        None
    }
  }

  override protected[fx] def update(updatedArc: VisualArc): Unit = {
    super.update(updatedArc)

    shaft.handles() =
      updatedArc.handles
  }
}
