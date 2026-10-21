package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.VertexNode
import scalafx.scene.shape.Line

private class LinkLine(sourceVertexNode: VertexNode, targetVertexNode: VertexNode) extends Line {
  styleClass.add("line")

  startX <==
    sourceVertexNode.centerX

  startY <==
    sourceVertexNode.centerY

  endX <==
    targetVertexNode.centerX

  endY <==
    targetVertexNode.centerY
}
