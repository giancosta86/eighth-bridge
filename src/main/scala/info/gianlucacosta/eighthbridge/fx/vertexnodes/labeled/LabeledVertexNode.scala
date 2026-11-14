package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import info.gianlucacosta.eighthbridge.graphs.facets.HasLabel
import scalafx.beans.property.ReadOnlyDoubleProperty

class LabeledVertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends VertexNode(graphCanvas, initialVertex) {

  override type V <:
    VisualVertex with HasLabel

  private val label =
    new VertexLabel(this)

  children.add(label)

  override val width: ReadOnlyDoubleProperty =
    label.width

  override val height: ReadOnlyDoubleProperty =
    label.height
}
