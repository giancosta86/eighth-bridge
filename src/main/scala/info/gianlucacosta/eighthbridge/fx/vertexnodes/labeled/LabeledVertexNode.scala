package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.core.facets.HasLabel
import info.gianlucacosta.eighthbridge.fx.RegionUtils._
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode, VisualVertex}
import scalafx.Includes._
import scalafx.beans.property.ReadOnlyDoubleProperty

class LabeledVertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends VertexNode(graphCanvas, initialVertex) {

  override type V <:
    VisualVertex with HasLabel

  private val label =
    new VertexLabel(vertex()) {
      vertex <==
        LabeledVertexNode.this.vertex
    }

  children.add(label)

  override val width: ReadOnlyDoubleProperty =
    label.width

  override val height: ReadOnlyDoubleProperty =
    label.height

  override protected def borderRadius: Double =
    label.basicBorderRadius
}
