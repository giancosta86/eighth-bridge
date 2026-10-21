package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.shape.Line
import scalafx.Includes._
import scalafx.beans.binding.Bindings

private class BarbSegment extends Line {
  styleClass.add("line")

  //TODO! All over the codebase, choose well the field type for props!
  val intersectionPoint: ObjectProperty[Option[Point2D]]  =
    new SimpleObjectProperty[Option[Point2D]](None)

  val sidePoint: ObjectProperty[Option[Point2D]] =
    new SimpleObjectProperty[Option[Point2D]](None)

  startX <==
    Bindings.createDoubleBinding(
      () =>
        intersectionPoint()
          .map(_.x)
          .getOrElse(0),

      intersectionPoint
    )

  startY <==
    Bindings.createDoubleBinding(
      () =>
        intersectionPoint()
          .map(_.y)
          .getOrElse(0),

      intersectionPoint
    )

  endX <==
    Bindings.createDoubleBinding(
      () =>
        sidePoint()
          .map(_.x)
          .getOrElse(0),

      sidePoint
    )

  endY <==
    Bindings.createDoubleBinding(
      () =>
        sidePoint()
          .map(_.y)
          .getOrElse(0),

      sidePoint
    )
}
