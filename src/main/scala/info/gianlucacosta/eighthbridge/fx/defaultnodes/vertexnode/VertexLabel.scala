package info.gianlucacosta.eighthbridge.fx.defaultnodes.vertexnode

import scalafx.scene.control.Label
import scalafx.scene.text.TextAlignment

class VertexLabel(vertexNode: DefaultVertexNode)
  extends Label {
  styleClass.add("label")

  text <==
    vertexNode.name

  layoutX <==
    vertexNode.centerX - width / 2

  layoutY <==
    vertexNode.centerY - height / 2
}
