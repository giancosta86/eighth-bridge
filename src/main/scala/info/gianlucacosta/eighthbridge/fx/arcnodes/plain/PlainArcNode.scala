package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{ArcNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.fx.graph.VisualArc
import scalafx.Includes._
import scalafx.geometry.Point2D

class PlainArcNode(graphCanvas: GraphCanvas, arc: VisualArc)
  extends ArcNode(graphCanvas, arc) {

  private val (sourceVertex, targetVertex) =
    graph.getAnchors(arc).get

  private val sourceVertexNode =
    graphCanvas.vertexNodesByVertex(sourceVertex)

  private val targetVertexNode =
    graphCanvas.vertexNodesByVertex(targetVertex)

  private val shaft =
    new Shaft(
      sourceVertexNode,
      targetVertexNode,
      onSegmentRightClicked =
        handleRightClickOnShaftSegment
    )

  children.add(shaft)

  private var barbsNode: Option[BarbsNode] =
    None

  arcBarbs.onChange(updateBarbSegments())

  private def updateBarbSegments(): Unit = {
    if (arcBarbs().isDefined && barbsNode.isEmpty) {
      val brandNewBarbsNode =
        new BarbsNode(sourceVertexNode, targetVertexNode, arc.barbs.get)

      barbsNode =
        Some(brandNewBarbsNode)

      children.add(brandNewBarbsNode)
    } else if (arcBarbs().isEmpty) {
      barbsNode.foreach(children.remove(_))

      barbsNode =
        None
    }
  }

  override protected[fx] def update(updatedArc: VisualArc): Unit = {
    super.update(updatedArc)

    println(s"When updating the plain arc node, the handles are: ${updatedArc.handles}")

    shaft.handles() =
      updatedArc.handles
  }

  private def handleRightClickOnShaftSegment(shaftSegmentIndex: Int, point: Point2D): Unit = {
    println(s"%%%%%%%%%%%% YOU HAVE CLICKED ON SEGMENT #${shaftSegmentIndex}")
    controller.createAddArcHandle(arc, shaftSegmentIndex, point)
      .foreach(graph_=)
  }
}
