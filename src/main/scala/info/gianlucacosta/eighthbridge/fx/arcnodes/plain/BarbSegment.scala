package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import javafx.beans.property.SimpleObjectProperty
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.shape.Line
import scalafx.Includes._
import scalafx.beans.binding.Bindings

private class BarbSegment(intersectionPoint: Point2D, sidePoint: Point2D) extends Line {
  styleClass.add("line")

  startX =
    intersectionPoint.x

  startY =
    intersectionPoint.y

  endX =
    sidePoint.x

  endY =
    sidePoint.y
}
