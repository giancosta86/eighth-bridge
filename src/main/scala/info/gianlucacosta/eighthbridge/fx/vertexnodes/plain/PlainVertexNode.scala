package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import scalafx.beans.property.ReadOnlyDoubleProperty

class PlainVertexNode(graphCanvas: GraphCanvas, vertex: VisualVertex) extends VertexNode(graphCanvas, vertex) {
  private val body =
    new VertexBody(this)

  children.add(body)

  override def width: ReadOnlyDoubleProperty =
    body.width

  override def height: ReadOnlyDoubleProperty =
    body.height
}
