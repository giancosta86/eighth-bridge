package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualLink}
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, LinkNode, VertexNode}
import info.gianlucacosta.eighthbridge.graphs.facets.LinkHandle
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

class PlainLinkNode(graphCanvas: GraphCanvas, initialLink: VisualLink)
  extends LinkNode(graphCanvas, initialLink) {

  protected val (sourceVertex, targetVertex) =
    graph.vertexPairByArc(link())

  protected val sourceVertexNode: VertexNode =
    graphCanvas.vertexNodesByVertex(sourceVertex)

  protected val targetVertexNode: VertexNode =
    graphCanvas.vertexNodesByVertex(targetVertex)

  private val shaft =
    new Shaft(
      sourceVertexNode,
      targetVertexNode,
      initialLink.handles,
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
      initialSourcePoint =
        shaft.lastPointBeforeTargetVertex(),
      initialIntersectionPoint =
        shaft.intersectionWithTargetVertexNode(),
      initialArcBarbs =
        initialLink.arcBarbs
    ) {
      sourcePoint <==
        shaft.lastPointBeforeTargetVertex

      intersectionPoint <==
        shaft.intersectionWithTargetVertexNode

      arcBarbs <==
        Bindings.createObjectBinding(
          () =>
            link().arcBarbs,

          link
    )
  }

  children.add(barbsNode)

  visible <==
    shaft.visible

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
