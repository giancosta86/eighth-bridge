package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.PointUtils.Point2DExtension
import info.gianlucacosta.eighthbridge.fx.linknodes.plain.PlainLinkNode.getActualBarbs
import info.gianlucacosta.eighthbridge.fx.{Delta2D, GraphCanvas, LinkNode, VertexNode, VisualLink}
import info.gianlucacosta.eighthbridge.core.directed.{Arc, ArcBarbs}
import info.gianlucacosta.eighthbridge.core.facets.{HasHandles, LinkHandle}
import info.gianlucacosta.eighthbridge.core.{Facet, Link}
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

object PlainLinkNode {
  def getActualBarbs(link: Link): Option[ArcBarbs] =
    Facet
      .as[Arc](link)
      .map(_.barbs)
}

class PlainLinkNode(graphCanvas: GraphCanvas, initialLink: VisualLink)
  extends LinkNode(graphCanvas, initialLink) {

  protected val (sourceVertex, targetVertex) =
    graph.getAnchorPair(link()).get

  protected val sourceVertexNode: VertexNode =
    graphCanvas.vertexNodesByVertex(sourceVertex)

  protected val targetVertexNode: VertexNode =
    graphCanvas.vertexNodesByVertex(targetVertex)

  private val initialHandles: List[LinkHandle] =
    Facet
      .as[HasHandles](initialLink)
      .map(_.handles)
      .getOrElse(Nil)

  private val shaft =
    new Shaft(
      sourceVertexNode,
      targetVertexNode,
      initialHandles,
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
            Facet
              .as[HasHandles](link())
              .map(_.handles)
              .getOrElse(Nil),

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
        getActualBarbs(initialLink)
    ) {
      sourcePoint <==
        shaft.lastPointBeforeTargetVertex

      intersectionPoint <==
        shaft.intersectionWithTargetVertexNode

      arcBarbs <==
        Bindings.createObjectBinding(
          () =>
            getActualBarbs(link()),

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

  private def handleDragOnHandleNode(handle: LinkHandle, delta: Delta2D): Unit = {
    controller.dragLinkHandle(link(), handle, delta)
  }

  override def bottomRight: Point2D =
    shaft.handles()
      .foldLeft(new Point2D(0, 0))((cumulatedPoint, handle) =>
        cumulatedPoint.mergeToBottomRight(handle.bottomRight)
      )
}
