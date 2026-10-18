package info.gianlucacosta.eighthbridge.fx.vertexnodes.labeled

import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.fx.{GraphCanvas, VertexNode}
import info.gianlucacosta.eighthbridge.theory.features.{Labeled, Named}
import javafx.beans.property.SimpleStringProperty
import scalafx.Includes._
import scalafx.beans.property.{ReadOnlyDoubleProperty, ReadOnlyStringProperty}
import scalafx.geometry.Point2D

class LabeledVertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex) extends VertexNode(graphCanvas, initialVertex) {
  private val _label =
    new SimpleStringProperty()

  val label: ReadOnlyStringProperty =
    _label

  private val labelControl =
    new VertexLabel(this)

  children.add(labelControl)


  override val width: ReadOnlyDoubleProperty =
    labelControl.width

  override val height: ReadOnlyDoubleProperty =
    labelControl.height

  override protected[fx] def update(updatedVertex: VisualVertex): Unit = {
    super.update(updatedVertex)

    _label() =
      updatedVertex.asInstanceOf[Labeled].label
  }
}
