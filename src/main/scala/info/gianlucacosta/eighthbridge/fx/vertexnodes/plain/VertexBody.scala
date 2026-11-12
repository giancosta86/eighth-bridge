package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.ReadOnlyProperties
import scalafx.Includes._
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.shape.Ellipse

private class VertexBody extends Ellipse {
  styleClass.add("ellipse")

  radiusX() =
    10

  radiusY() =
    10

  val width: ReadOnlyDoubleProperty =
    ReadOnlyProperties.forDouble(
      () =>
        radiusX() * 2,

      radiusX
    )

  val height: ReadOnlyDoubleProperty =
    ReadOnlyProperties.forDouble(
      () =>
        radiusY() * 2,

      radiusY
    )
}
