package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.core.facets.HasLabel
import info.gianlucacosta.eighthbridge.fx.VisualVertex
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.scene.control.Label

private class VertexLabel(initialVertex: VisualVertex with HasLabel) extends Label {
  styleClass.add("label")

  val vertex: ObjectProperty[VisualVertex with HasLabel] =
    new SimpleObjectProperty[VisualVertex with HasLabel](initialVertex)

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
