package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{Centered, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.binding.{Bindings, ObjectBinding}
import scalafx.scene.Group
import scalafx.Includes._
import scalafx.beans.property.{ReadOnlyDoubleProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper}
import scalafx.geometry.Point2D

private class Shaft(
                     sourceVertexNode: VertexNode,
                     targetVertexNode: VertexNode,
                     initialHandles: List[ArcHandle],
                     onSegmentRightClicked: (Int, Point2D) => Unit
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

  handles.onInvalidate {
    println(s"The handles are: ${handles()}")
  }

  private val handleNodeGroup =
    new HandleNodeGroup(initialHandles)

  children.add(handleNodeGroup)


  private val pointProviders =
    Bindings.createObjectBinding[List[Centered]](
      () =>
          List(sourceVertexNode) ++ handleNodeGroup.handleNodes() ++ List(targetVertexNode),

      handleNodeGroup.handleNodes
    )

  private val polyline =
    new ShaftPolyline(pointProviders(), onSegmentRightClicked) {
      pointProviders <==
        self.pointProviders
    }

  children.add(polyline)

  val arrowSourcePointProvider: ObjectBinding[Centered] =
    Bindings.createObjectBinding[Centered](
      () =>
        pointProviders().takeRight(2).head,

      pointProviders
    )






  /*
  handles.onChange {
    val updatedHandles =
      handles()

    val currentHandleNodes =
      handleNodes()

    val handleNodesToRemove =
      currentHandleNodes
        .filter(handleNode =>
          !updatedHandles.contains(handleNode.handle)
        )
        .toSet

    handleNodesToRemove.foreach(
      children.remove(_)
    )

    val keptHandleNodes: List[HandleNode] =
      currentHandleNodes.filter(
        !handleNodesToRemove.contains(_)
      )

    val updatedHandleNodes: List[HandleNode] =
      updatedHandles
        .zipWithIndex
        .map { case (handle, index) =>
          val handleNode =
            keptHandleNodes
              .find(_.handle == handle)
              .getOrElse {
                val brandNewHandleNode =
                  new HandleNode(handle)

                children.add(brandNewHandleNode)

                brandNewHandleNode
              }

          handleNode.update(handle)

          handleNode
        }

    handleNodes() =
      updatedHandleNodes
  }


  private val segmentPointProviders =
    new SimpleObjectProperty[List[Centered]]

  segmentPointProviders <== Bindings.createObjectBinding(
    () =>
      List(sourceVertexNode) ++
        handleNodes() ++
        List(targetVertexNode),

    handleNodes
  )

  private val segments =
    new SimpleObjectProperty[List[ShaftSegment]](Nil)

  segments <== Bindings.createObjectBinding(
    () => {
      segmentPointProviders()
        .sliding(2)
        .zipWithIndex
        .map {
          case (startProvider::endProvider::Nil, index) =>
            new ShaftSegment(
              startProvider,
              endProvider,
              onRightClick = (point) => {
                onSegmentRightClicked(index, point)
              }
            )
          case _ =>
            throw new Exception("This should not occur!")
        }
        .toList
    },

    segmentPointProviders
  )

  segments.onChange((_, oldValue, newValue) => {
    redrawSegments(oldValue, newValue)
  })

  private def redrawSegments(oldSegments: List[ShaftSegment], newSegments: List[ShaftSegment]): Unit = {
    println(s"-----------> THE ARC SEGMENTS ARE: ${segments().size}")

    children.removeRange(0, oldSegments.size)

    newSegments
      .reverse
      .foreach(children.add(0, _))
  }

  redrawSegments(List(), segments()) */
}
