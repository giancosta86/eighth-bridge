package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._

import scala.collection.JavaConversions._
import scalafx.beans.property.{ReadOnlyBooleanProperty, ReadOnlyDoubleProperty}
import scalafx.scene.Group
import scalafx.scene.layout.Region

/**
  * JavaFX node rendering a VisualVertex
  */
abstract class VertexNode
  extends Group with GraphCanvasNode {
  private val _centerX =
    new SimpleDoubleProperty()

  val centerX: ReadOnlyDoubleProperty =
    _centerX

  private val _centerY =
    new SimpleDoubleProperty()

  val centerY: ReadOnlyDoubleProperty =
    _centerY

  def width: ReadOnlyDoubleProperty
  def height: ReadOnlyDoubleProperty

  private val _selected =
    new SimpleBooleanProperty(false)

  val selected: ReadOnlyBooleanProperty =
    _selected

  layoutX <==
    _centerX - width / 2

  layoutY <==
    _centerY - height / 2

  protected[fx] def updateFxProps(vertex: VisualVertex): Unit = {
    _centerX() =
      vertex.center.x

    _centerY() =
      vertex.center.y

    _selected() =
      vertex.selected

    styleClass.setAll("vertex")
    styleClass.addAll(vertex.styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      vertex.selected
    )
  }
}
