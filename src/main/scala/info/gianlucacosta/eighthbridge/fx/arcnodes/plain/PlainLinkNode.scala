package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{LinkNode, GraphCanvas}
import info.gianlucacosta.eighthbridge.fx.graph.{ArcBarbs, LinkHandle, Delta, VisualLink}
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

class PlainLinkNode(graphCanvas: GraphCanvas, initialArc: VisualLink)
  extends LinkNode(graphCanvas, initialArc) {

  private val (sourceVertex, targetVertex) =
    graph.getAnchors(arc()).get

  protected val sourceVertexNode =
    graphCanvas.vertexNodesByVertex(sourceVertex)

  protected val targetVertexNode =
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
    controller.addLinkHandle(arc(), shaftSegmentIndex, point)
  }

  private def handleRightClickOnHandleNode(handle: LinkHandle): Unit = {
    println(s"@@@@@@@@@@@ YOU HAVE RIGHT-CLICKED ON HANDLE-NODE FOR HANDLE ${handle}")
    controller.deleteLinkHandle(arc(), handle)
  }

  private def handleDragOnHandleNode(handle: LinkHandle, delta: Delta): Unit = {
    //println(s"///////////////// YOU ARE DRAGGING ${handle} to ${updatedCenter}")
    controller.dragLinkHandle(arc(), handle, delta)
  }
}
