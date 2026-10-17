package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import scalafx.scene.control.Label

private class VertexLabel(vertexNode: LabeledVertexNode)
  extends Label {
  styleClass.add("label")

  text <==
    vertexNode.label

  layoutX <==
    vertexNode.centerX - width / 2

  layoutY <==
    vertexNode.centerY - height / 2
}
