package info.gianlucacosta.eighthbridge.fx.vertexnodes.named

import scalafx.scene.control.Label

private class VertexLabel(vertexNode: NamedVertexNode)
  extends Label {
  styleClass.add("label")

  text <==
    vertexNode.name

  layoutX <==
    vertexNode.centerX - width / 2

  layoutY <==
    vertexNode.centerY - height / 2
}
