package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.VertexNode
import scalafx.scene.Group

private class Shaft(sourceVertexNode: VertexNode, targetVertexNode: VertexNode) extends Group {
  private val line =
    new ShaftSegment(sourceVertexNode, targetVertexNode)

  children.add(line)
}
