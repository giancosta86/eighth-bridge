package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.shape.Line

private class BarbSegment extends Line {
  styleClass.add("line")

  val intersectionPoint: ObjectProperty[Point2D] =
    new SimpleObjectProperty[Point2D](new Point2D(0, 0))

  val sidePoint: ObjectProperty[Point2D] =
    new SimpleObjectProperty[Point2D](new Point2D(0, 0))

  startX <==
    Bindings.createDoubleBinding(
      () =>
        intersectionPoint().x,

      intersectionPoint
    )

  startY <==
    Bindings.createDoubleBinding(
      () =>
        intersectionPoint().y,

      intersectionPoint
    )

  endX <==
    Bindings.createDoubleBinding(
      () =>
        sidePoint().x,

      sidePoint
    )

  endY <==
    Bindings.createDoubleBinding(
      () =>
        sidePoint().y,

      sidePoint
    )
}
