package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualLink}
import info.gianlucacosta.eighthbridge.fx.linknodes.plain.PlainLinkNode.getActualBarbs
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, LinkNode, VertexNode}
import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.directed.{Arc, ArcBarbs}
import info.gianlucacosta.eighthbridge.graphs.facets.LinkHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

object PlainLinkNode {
  def getActualBarbs(link: Link): Option[ArcBarbs] =
      link match {
        case arc: Arc =>
          Some(arc.barbs)

        case _ =>
          None
      }
}

class PlainLinkNode(graphCanvas: GraphCanvas, initialLink: VisualLink)
  extends LinkNode(graphCanvas, initialLink) {

  protected val (sourceVertex, targetVertex) =
    graph.getAnchorPair(link()).get

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

  private val actualBarbs =
    new SimpleObjectProperty[Option[ArcBarbs]](

    )

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

  private def handleDragOnHandleNode(handle: LinkHandle, delta: Delta): Unit = {
    controller.dragLinkHandle(link(), handle, delta)
  }
}
