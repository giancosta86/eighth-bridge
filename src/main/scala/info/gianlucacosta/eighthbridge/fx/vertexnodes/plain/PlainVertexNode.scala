package info.gianlucacosta.eighthbridge.fx.vertexnodes.plain

import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode, VisualVertex}
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ReadOnlyDoubleProperty

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

  override val width: ReadOnlyDoubleProperty =
    body.width

  override val height: ReadOnlyDoubleProperty =
    body.height
}
