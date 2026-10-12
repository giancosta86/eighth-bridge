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
abstract class VertexNode(initialVertex: VisualVertex)
  extends Group with GraphCanvasNode {
  private var currentVertex: VisualVertex =
    initialVertex

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


  private[fx] def currentVertex_=(updatedVertex: VisualVertex): Unit = {
    if (currentVertex.visualEquals(updatedVertex)) {
      updateFxProps(updatedVertex)
    }

    this.currentVertex = updatedVertex
  }

  protected def updateFxProps(currentVertex: VisualVertex): Unit = {
    _centerX() =
      currentVertex.center.x

    _centerY() =
      currentVertex.center.y

    _selected() =
      currentVertex.selected

    styleClass.setAll("vertex")
    styleClass.addAll(currentVertex.styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      currentVertex.selected
    )
  }

  updateFxProps(initialVertex)
}
