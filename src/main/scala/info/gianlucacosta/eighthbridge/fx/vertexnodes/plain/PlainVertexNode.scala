package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.VertexNode
import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import scalafx.beans.property.ReadOnlyDoubleProperty

class PlainVertexNode(vertex: VisualVertex) extends VertexNode(vertex) {
  //TODO! Here, just an ellipse

  override def width: ReadOnlyDoubleProperty = ???

  override def height: ReadOnlyDoubleProperty = ???
}
