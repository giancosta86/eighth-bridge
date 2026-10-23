package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{Centered, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.binding.{Bindings, ObjectBinding}
import scalafx.scene.Group
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, ReadOnlyDoubleProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper}
import scalafx.geometry.Point2D

private class Shaft(
                     sourceVertexNode: VertexNode,
                     targetVertexNode: VertexNode,
                     initialHandles: List[ArcHandle],
                     onSegmentRightClick: SegmentRightClickHandler,
                     onHandleNodeRightClick: HandleNodeRightClickHandler
                   ) extends Group {
  /*
  private val line =
    new ShaftSegment(sourceVertexNode, targetVertexNode, _ => {})

  children.add(line)
  */

  private val self =
    this

  //TODO: for private classes, just use private properties when change from outside is required

  val handles =
    new SimpleObjectProperty[List[ArcHandle]](initialHandles)

  private val handleNodeGroup =
    new HandleNodeGroup(
      initialHandles,
      onHandleNodeRightClick =
        onHandleNodeRightClick
    ) {
      handles <==
        self.handles
    }

  private val pointProviders =
    Bindings.createObjectBinding[List[Centered]](
      () =>
          List(sourceVertexNode) ++ handleNodeGroup.handleNodes() ++ List(targetVertexNode),

      handleNodeGroup.handleNodes
    )

  private val polyline =
    new ShaftPolyline(
      pointProviders(),
      onSegmentRightClick =
        onSegmentRightClick
    ) {
      pointProviders <==
        self.pointProviders
    }

  val arrowSourcePointProvider: ObjectBinding[Centered] =
    Bindings.createObjectBinding[Centered](
      () =>
        pointProviders().takeRight(2).head,

      pointProviders
    )

  children.add(polyline)
  children.add(handleNodeGroup)
}
