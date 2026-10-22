package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{ArcNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.fx.graph.{ArcBarbs, VisualArc}
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

class PlainArcNode(graphCanvas: GraphCanvas, initialArc: VisualArc)
  extends ArcNode(graphCanvas, initialArc) {

  private val (sourceVertex, targetVertex) =
    graph.getAnchors(arc()).get

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
    ) {
      //TODO! Use this compact notation more!
      handles <==
        Bindings.createObjectBinding(
          () =>
            arc().handles,

          arc
        )
    }

  children.add(shaft)

  private val arcBarbs =
    new SimpleObjectProperty[Option[ArcBarbs]]

  arcBarbs <==
    Bindings.createObjectBinding(
      () =>
        arc().barbs,

      arc
    )

  private var barbsNode: Option[BarbsNode] =
    None

  arcBarbs.onChange(updateBarbSegments())

  private def updateBarbSegments(): Unit = {
    if (arcBarbs().isDefined && barbsNode.isEmpty) {
      val brandNewBarbsNode =
        new BarbsNode(sourceVertexNode, targetVertexNode, arc().barbs.get)

      barbsNode =
        Some(brandNewBarbsNode)

      children.add(brandNewBarbsNode)
    } else if (arcBarbs().isEmpty) {
      barbsNode.foreach(children.remove(_))

      barbsNode =
        None
    }
  }


  private def handleRightClickOnShaftSegment(shaftSegmentIndex: Int, point: Point2D): Unit = {
    println(s"%%%%%%%%%%%% YOU HAVE CLICKED ON SEGMENT #${shaftSegmentIndex}")
    controller.createAddArcHandle(arc(), shaftSegmentIndex, point)
      .foreach(graph_=)
  }
}
