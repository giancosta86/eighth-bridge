package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.ReadOnlyProperties
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.shape.Ellipse

private class VertexBody extends Ellipse {
  styleClass.add("ellipse")

  val radius =
    10

  radiusX() =
    radius

  radiusY() =
    radius

  val width: ReadOnlyDoubleProperty =
    ReadOnlyProperties.forDouble(
      () =>
        radius * 2
    )

  val height: ReadOnlyDoubleProperty =
    ReadOnlyProperties.forDouble(
      () =>
        radius * 2
    )
}
