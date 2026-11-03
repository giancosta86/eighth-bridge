package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.shape.Ellipse
import scalafx.Includes._

class PlainVertexNode(graphCanvas: GraphCanvas, vertex: VisualVertex) extends VertexNode(graphCanvas, vertex) {
  private val body =
    new VertexBody(this)

  children.add(body)

  override def width: ReadOnlyDoubleProperty =
    body.width

  override def height: ReadOnlyDoubleProperty =
    body.height
}
