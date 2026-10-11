package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.{SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._

import scala.collection.JavaConversions._
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.scene.Group

/**
  * JavaFX node rendering a VisualVertex
  */
abstract class VertexNode extends Group with GraphCanvasNode {
  val vertex: VisualVertex

  private val currentVertex: SimpleObjectProperty[VisualVertex] =
    new SimpleObjectProperty(vertex)

  currentVertex.addListener((_: javafx.beans.Observable) => {
    updateFx()
  })

  protected val centerX =
    new SimpleDoubleProperty(vertex.center.x)

  protected val centerY =
    new SimpleDoubleProperty(vertex.center.y)

  def width: ReadOnlyDoubleProperty

  def height: ReadOnlyDoubleProperty

  protected[fx] def updateFx(): Unit = {
    styleClass.setAll("vertex")
    styleClass.addAll(currentVertex().styleClasses)

    centerX() = currentVertex().center.x
    centerY() = currentVertex().center.y

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      currentVertex().selected
    )
  }
}
