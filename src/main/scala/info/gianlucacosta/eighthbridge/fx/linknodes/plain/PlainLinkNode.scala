package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, LinkNode, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.{ArcBarbs, Delta, LinkHandle, VisualLink}
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

class PlainLinkNode(graphCanvas: GraphCanvas, initialArc: VisualLink)
  extends LinkNode(graphCanvas, initialArc) {

  protected val (sourceVertex, targetVertex) =
    graph.getAnchors(link()).get

  protected val sourceVertexNode: VertexNode =
    graphCanvas.vertexNodesByVertex(sourceVertex)

  protected val targetVertexNode: VertexNode =
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
            link().handles,

          link
        )
    }

  children.add(shaft)


  private val barbsNode =
    new BarbsNode(
      shaft.arrowSourcePoint(),
      targetVertexNode,
      initialArc.barbs
    ) {
      sourcePoint <==
        shaft.arrowSourcePoint

      arcBarbs <==
        Bindings.createObjectBinding(
          () =>
            link().barbs,

          link
    )
  }

  children.add(barbsNode)

  private def handleRightClickOnShaftSegment(shaftSegmentIndex: SegmentIndex, point: Point2D): Unit = {
    println(s"%%%%%%%%%%%% YOU HAVE RIGHT-CLICKED ON SEGMENT #${shaftSegmentIndex}")
    controller.addLinkHandle(link(), shaftSegmentIndex, point)
  }

  private def handleRightClickOnHandleNode(handle: LinkHandle): Unit = {
    println(s"@@@@@@@@@@@ YOU HAVE RIGHT-CLICKED ON HANDLE-NODE FOR HANDLE ${handle}")
    controller.deleteLinkHandle(link(), handle)
  }

  private def handleDragOnHandleNode(handle: LinkHandle, delta: Delta): Unit = {
    controller.dragLinkHandle(link(), handle, delta)
  }
}
