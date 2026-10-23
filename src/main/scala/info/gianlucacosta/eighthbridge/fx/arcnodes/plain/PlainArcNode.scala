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
      initialArc.handles,
      onSegmentRightClicked =
        handleRightClickOnShaftSegment
    ) {
      handles <==
        Bindings.createObjectBinding(
          () =>
            arc().handles,

          arc
        )
    }

  children.add(shaft)


  private val barbsNode =
    new BarbsNode(
      shaft.arrowSourcePointProvider(),
      targetVertexNode,
      initialArc.barbs
    ) {
      sourcePointProvider <==
        shaft.arrowSourcePointProvider

      arcBarbs <==
        Bindings.createObjectBinding(
          () =>
            arc().barbs,

          arc
    )
  }

  children.add(barbsNode)

  private def handleRightClickOnShaftSegment(shaftSegmentIndex: Int, point: Point2D): Unit = {
    println(s"%%%%%%%%%%%% YOU HAVE CLICKED ON SEGMENT #${shaftSegmentIndex}")
    /*controller.createAddArcHandle(arc(), shaftSegmentIndex, point)
      .foreach(graph_=)*/
  }
}
