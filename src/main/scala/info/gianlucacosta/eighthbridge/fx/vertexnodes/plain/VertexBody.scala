package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.ReadOnlyProperties
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.shape.Ellipse

private class VertexBody(radius: Double) extends Ellipse {
  styleClass.add("ellipse")

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
