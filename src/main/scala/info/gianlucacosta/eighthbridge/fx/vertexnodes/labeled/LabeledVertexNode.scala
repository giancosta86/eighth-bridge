package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import info.gianlucacosta.eighthbridge.theory.features.{Labeled, Named}
import javafx.beans.property.SimpleStringProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ReadOnlyDoubleProperty, ReadOnlyStringProperty}
import scalafx.geometry.Point2D

class LabeledVertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends VertexNode(graphCanvas, initialVertex) {

  private val label =
    new VertexLabel(this)

  children.add(label)

  override val width: ReadOnlyDoubleProperty =
    label.width

  override val height: ReadOnlyDoubleProperty =
    label.height
}
