package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{Centered, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.ArcHandle
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.binding.Bindings
import scalafx.scene.Group
import scalafx.Includes._

private class Shaft(sourceVertexNode: VertexNode, targetVertexNode: VertexNode) extends Group {
  /*private val line =
    new ShaftSegment(sourceVertexNode, targetVertexNode)

  children.add(line)*/

  private[plain] val handles =
    new SimpleObjectProperty[List[ArcHandle]](Nil)

  private val handlePoints =
    new SimpleObjectProperty[List[HandleNode]](Nil)

  private val segmentPointProviders =
    new SimpleObjectProperty[List[Centered]]

  segmentPointProviders <== Bindings.createObjectBinding(
    () =>
      List(sourceVertexNode) ++
        handlePoints() ++
        List(targetVertexNode),

    handlePoints
  )

  val segments =
    new SimpleObjectProperty[List[ShaftSegment]](Nil)

  segments <== Bindings.createObjectBinding(
    () => {
      segmentPointProviders()
        .sliding(2)
        .map {
          case startProvider::endProvider::Nil =>
            new ShaftSegment(startProvider, endProvider)
          case _ =>
            throw new Exception("This should not occur!")
        }
        .toList
    },

    segmentPointProviders
  )

  println(s"-----------> THE ARC SEGMENTS ARE: ${segments().size}")

  segments.onChange((_, oldValue, newValue) => {
    redrawSegments(oldValue, newValue)
  })

  private def redrawSegments(oldSegments: List[ShaftSegment], newSegments: List[ShaftSegment]): Unit = {
    children.removeRange(0, oldSegments.size)

    newSegments
      .reverse
      .foreach(children.add(0, _))
  }

  redrawSegments(List(), segments())
}
