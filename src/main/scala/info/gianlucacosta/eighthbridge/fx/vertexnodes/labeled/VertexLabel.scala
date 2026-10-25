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
    vertexNode.centerX - width / 2

  layoutY <==
    vertexNode.centerY - height / 2

  text <== Bindings.createStringBinding(
    () =>
      vertexNode
        .vertex()
        .asInstanceOf[Labeled]
        .label,

    vertexNode.vertex
  )
}
