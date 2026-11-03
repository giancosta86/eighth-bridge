package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.features.Labeled
import scalafx.beans.binding.Bindings
import scalafx.scene.control.Label
import scalafx.Includes._

private class VertexLabel(vertexNode: LabeledVertexNode)
  extends Label {
  styleClass.add("label")

  layoutX <==
    Bindings.createDoubleBinding(
      () =>
        vertexNode.vertex().center.x - width() / 2,

      vertexNode.vertex,
      width
    )


  layoutY <==
    Bindings.createDoubleBinding(
      () =>
        vertexNode.vertex().center.y - height() / 2,

      vertexNode.vertex,
      height
    )


  text <== Bindings.createStringBinding(
    () =>
      vertexNode
        .vertex()
        .label,

    vertexNode.vertex
  )
}
