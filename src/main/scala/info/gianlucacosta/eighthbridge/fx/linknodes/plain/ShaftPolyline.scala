package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.Includes._

private class ShaftPolyline(
                             initialSegmentPoints: List[Point2D],
                             onSegmentRightClick: SegmentRightClickHandler
                           ) extends Group {

  val segmentPoints: ObjectProperty[List[Point2D]] =
    new SimpleObjectProperty[List[Point2D]](initialSegmentPoints)

  private val segments =
    Bindings.createObjectBinding[List[ShaftSegment]](
      () =>
        segmentPoints()
          .sliding(2)
          .zipWithIndex
          .map {
            case (startPoint::endPoint::Nil, index) =>
              new ShaftSegment(
                startPoint,
                endPoint,
                onRightClick = (point) => {
                  onSegmentRightClick(index, point)
                }
              )
            case _ =>
              throw new Exception("This should not occur!")
          }
          .toList,

      segmentPoints
    )

  segments.onChange(updateSegmentNodes())

  updateSegmentNodes()

  private def updateSegmentNodes(): Unit = {
    children.clear()

    segments().foreach(children.add(_))
  }
}
