package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{Centered, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.binding.Bindings
import scalafx.scene.Group
import scalafx.Includes._
import scalafx.beans.property.{ReadOnlyDoubleProperty, ReadOnlyObjectProperty, ReadOnlyObjectWrapper}
import scalafx.geometry.Point2D

private class Shaft(
                     sourceVertexNode: VertexNode,
                     targetVertexNode: VertexNode,
                     onSegmentRightClicked: (Int, Point2D) => Unit
                   ) extends Group {
  private val line =
    new ShaftSegment(sourceVertexNode, targetVertexNode, _ => {})

  children.add(line)

  val arrowSourcePointProvider: ReadOnlyObjectProperty[Centered] =
    new SimpleObjectProperty[Centered](sourceVertexNode)

  /*
  //TODO: for private classes, just use private properties when change from outside is required
  val handles =
    new SimpleObjectProperty[List[ArcHandle]](Nil)

  private val handleNodes =
    new SimpleObjectProperty[List[HandleNode]](Nil)

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
