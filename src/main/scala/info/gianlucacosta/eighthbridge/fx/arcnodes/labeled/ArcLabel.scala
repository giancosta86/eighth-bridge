package info.gianlucacosta.eighthbridge.fx.arcnodes.labeled

import javafx.beans.property.{SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.beans.property.{DoubleProperty, ObjectProperty}
import scalafx.scene.control.Label
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D

private class ArcLabel extends Label {
  styleClass.add("label")

  val center: ObjectProperty[Point2D] =
    new SimpleObjectProperty[Point2D]

  layoutX <==
    Bindings.createDoubleBinding(
      () =>
        center().x - width() / 2,

      center,
      width
    )

  layoutY <==
    Bindings.createDoubleBinding(
      () =>
        center().y - height() / 2,

      center,
      height
    )
}

