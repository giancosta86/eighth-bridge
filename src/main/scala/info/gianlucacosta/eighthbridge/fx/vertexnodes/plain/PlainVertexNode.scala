package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.Includes._

class PlainVertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex) extends VertexNode(graphCanvas, initialVertex) {
  private val body =
    new VertexBody {
      centerX <==
        Bindings.createDoubleBinding(
          () =>
            vertex().center.x,

          vertex
        )

      centerY <==
        Bindings.createDoubleBinding(
          () =>
            vertex().center.y,

          vertex
        )
    }

  children.add(body)

  override def width: ReadOnlyDoubleProperty =
    body.width

  override def height: ReadOnlyDoubleProperty =
    body.height
}
