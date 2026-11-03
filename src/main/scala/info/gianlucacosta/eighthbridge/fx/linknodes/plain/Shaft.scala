package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.{Segments, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.LinkHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.binding.{Bindings, ObjectBinding}
import scalafx.scene.Group
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, ReadOnlyDoubleProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper}
import scalafx.geometry.{Dimension2D, Point2D}

private class Shaft(
                     sourceVertexNode: VertexNode,
                     targetVertexNode: VertexNode,
                     initialHandles: List[LinkHandle],
                     onSegmentRightClick: SegmentRightClickHandler,
                     onHandleNodeRightClick: HandleNodeRightClickHandler,
                     onHandleNodeDrag: HandleNodeDragHandler
                   ) extends Group {
  val handles =
    new SimpleObjectProperty[List[LinkHandle]](initialHandles)

  private val firstPointAfterSourceVertex =
    Bindings.createObjectBinding[Point2D](
      () =>
        handles()
          .headOption
          .map(_.center)
          .getOrElse(
            targetVertexNode.vertex().center
          ),

      handles,
      targetVertexNode.vertex
    )

  private val intersectionWithSourceVertexNode =
    Bindings.createObjectBinding[Option[Point2D]](
      () =>
        sourceVertexNode.getIntersectionWithSegmentFromPoint(
          firstPointAfterSourceVertex()
        ),

      firstPointAfterSourceVertex,
      sourceVertexNode.vertex,
      sourceVertexNode.width,
      sourceVertexNode.height
    )

  val lastPointBeforeTargetVertex: ObjectBinding[Point2D] =
    Bindings.createObjectBinding[Point2D](
      () =>
        handles()
          .lastOption
          .map(_.center)
          .getOrElse(sourceVertexNode.vertex().center),

      handles,
      sourceVertexNode.vertex
    )

  val intersectionWithTargetVertexNode: ObjectBinding[Option[Point2D]] =
    Bindings.createObjectBinding[Option[Point2D]](
      () =>
        targetVertexNode.getIntersectionWithSegmentFromPoint(
          lastPointBeforeTargetVertex()
        ),

      lastPointBeforeTargetVertex,
      targetVertexNode.vertex,
      targetVertexNode.width,
      targetVertexNode.height
    )

  private val segmentPoints =
    Bindings.createObjectBinding[List[Point2D]](
      () =>
        intersectionWithSourceVertexNode().flatMap(intersectionWithSourceVertexNode =>
          intersectionWithTargetVertexNode().map(intersectionWithTargetVertexNode =>
            List(intersectionWithSourceVertexNode) ++
              handles().map(_.center) ++
              List(intersectionWithTargetVertexNode)
          )
        )
          .getOrElse(Nil),

      intersectionWithSourceVertexNode,
      handles,
      intersectionWithTargetVertexNode
    )

  private val polyline =
    new ShaftPolyline(
      segmentPoints(),
      onSegmentRightClick =
        onSegmentRightClick
    ) {
      segmentPoints <==
        Shaft.this.segmentPoints
    }


  children.add(polyline)

  private val handleNodeGroup =
    new HandleNodeGroup(
      initialHandles,
      onHandleNodeRightClick =
        onHandleNodeRightClick,
      onHandleNodeDrag =
        onHandleNodeDrag
    ) {
      handles <==
        Shaft.this.handles
    }

  children.add(handleNodeGroup)
}
