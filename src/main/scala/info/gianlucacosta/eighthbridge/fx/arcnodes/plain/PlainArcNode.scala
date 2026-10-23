package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{ArcNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.fx.graph.{ArcBarbs, ArcHandle, Delta, VisualArc}
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
      onSegmentRightClick =
        handleRightClickOnShaftSegment,
      onHandleNodeRightClick =
        handleRightClickOnHandleNode,
      onHandleNodeDrag =
        handleDragOnHandleNode
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
    println(s"%%%%%%%%%%%% YOU HAVE RIGHT-CLICKED ON SEGMENT #${shaftSegmentIndex}")
    controller.addArcHandle(arc(), shaftSegmentIndex, point)
      .foreach(graph_=)
  }

  private def handleRightClickOnHandleNode(handle: ArcHandle): Unit = {
    println(s"@@@@@@@@@@@ YOU HAVE RIGHT-CLICKED ON HANDLE-NODE FOR HANDLE ${handle}")
    controller.removeArcHandle(arc(), handle)
      .foreach(graph_=)
  }

  private def handleDragOnHandleNode(handle: ArcHandle, updatedCenter: Point2D): Unit = {
    //println(s"///////////////// YOU ARE DRAGGING ${handle} to ${updatedCenter}")
    controller.dragArcHandle(arc(), handle, updatedCenter)
      .foreach(graph_=)
  }
}
