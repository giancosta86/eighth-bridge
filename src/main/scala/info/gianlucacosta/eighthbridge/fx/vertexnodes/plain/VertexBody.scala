package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.ReadOnlyProperties
import scalafx.Includes._
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.shape.Ellipse

private class VertexBody(vertexNode: PlainVertexNode) extends Ellipse {
  styleClass.add("ellipse")

  radiusX() =
    10

  radiusY() =
    10

  layoutX <==
    vertexNode.vertex().center.x

  layoutY <==
    vertexNode.vertex().center.y

  val width: ReadOnlyDoubleProperty =
    ReadOnlyProperties.createDouble(
      () =>
        radiusX() * 2,

      radiusX
    )

  val height: ReadOnlyDoubleProperty =
    ReadOnlyProperties.createDouble(
      () =>
        radiusY() * 2,

      radiusY
    )
}
