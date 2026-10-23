package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.Centered
import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.Includes._

private class ShaftPolyline(
                             initialPointProviders: List[Centered],
                             onSegmentRightClick: SegmentRightClickHandler
                           ) extends Group {

  val pointProviders: ObjectProperty[List[Centered]] =
    new SimpleObjectProperty[List[Centered]](initialPointProviders)

  private val segments =
    Bindings.createObjectBinding[List[ShaftSegment]](
      () =>
        pointProviders()
          .sliding(2)
          .zipWithIndex
          .map {
            case (startProvider::endProvider::Nil, index) =>
              new ShaftSegment(
                startProvider,
                endProvider,
                onRightClick = (point) => {
                  onSegmentRightClick(index, point)
                }
              )
            case _ =>
              throw new Exception("This should not occur!")
          }
          .toList,

      pointProviders
    )

  segments.onChange(updateSegmentNodes())

  updateSegmentNodes()

  private def updateSegmentNodes(): Unit = {
    println(s"############## The segments are: ${segments().size}")
    children.clear()

    segments().foreach(children.add(_))
  }
}
