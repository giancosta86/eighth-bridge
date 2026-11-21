package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode, VisualVertex}
import info.gianlucacosta.eighthbridge.core.facets.HasLabel
import scalafx.beans.property.ReadOnlyDoubleProperty
import info.gianlucacosta.eighthbridge.fx.RegionUtils._
import scalafx.beans.binding.Bindings
import scalafx.scene.control.Label
import scalafx.Includes._

class LabeledVertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends VertexNode(graphCanvas, initialVertex) {

  override type V <:
    VisualVertex with HasLabel

  private val label =
    new Label {
      styleClass.add("label")

      layoutX <==
        Bindings.createDoubleBinding(
          () =>
            vertex().center.x - width() / 2,

          vertex,
          width
        )

      layoutY <==
        Bindings.createDoubleBinding(
          () =>
            vertex().center.y - height() / 2,

          vertex,
          height
        )

      text <== Bindings.createStringBinding(
        () =>
          vertex().label,

        vertex
      )
    }

  children.add(label)

  override val width: ReadOnlyDoubleProperty =
    label.width

  override val height: ReadOnlyDoubleProperty =
    label.height

  override protected def borderRadius: Double =
    label.basicBorderRadius
}
