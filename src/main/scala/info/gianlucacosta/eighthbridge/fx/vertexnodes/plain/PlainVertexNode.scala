package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import scalafx.beans.property.ReadOnlyDoubleProperty

class PlainVertexNode(graphCanvas: GraphCanvas, vertex: VisualVertex) extends VertexNode(graphCanvas, vertex) {
  //TODO! Here, just an ellipse

  override def width: ReadOnlyDoubleProperty = ???

  override def height: ReadOnlyDoubleProperty = ???
}
