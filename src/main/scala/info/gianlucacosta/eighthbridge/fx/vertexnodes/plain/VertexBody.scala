package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.ReadOnlyProperties
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.layout.Pane
import scalafx.scene.shape.Ellipse

private class VertexBody(vertexNode: PlainVertexNode) extends Ellipse {
  styleClass.add("ellipse")

  radiusX() =
    10

  radiusY() =
    10

  layoutX <==
    vertexNode.centerX

  layoutY <==
    vertexNode.centerY

  val width =
    ReadOnlyProperties.createDouble(
      () =>
        radiusX() * 2,

      radiusX
    )

  val height =
    ReadOnlyProperties.createDouble(
      () =>
        radiusY() * 2,

      radiusY
    )
}
