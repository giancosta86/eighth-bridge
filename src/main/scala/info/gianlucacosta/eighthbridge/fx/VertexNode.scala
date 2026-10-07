package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleDoubleProperty

import scala.collection.JavaConversions._
import scalafx.beans.property.ReadOnlyDoubleProperty

/**
  * JavaFX node rendering a VisualVertex
  */
trait VertexNode[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] extends GraphCanvasNode[V, L, G] {
  private var _vertex: V = _

  /**
    * The underlying vertex, updated as rendering is performed
    *
    * @return
    */
  def vertex: V =
    _vertex


  private[fx] def vertex_=(newVertex: V): Unit =
    _vertex = newVertex

  protected val centerX =
    new SimpleDoubleProperty(0)

  protected val centerY =
    new SimpleDoubleProperty(0)


  def width: ReadOnlyDoubleProperty

  def height: ReadOnlyDoubleProperty


  override def render(): Unit = {
    styleClass.setAll("vertex")
    styleClass.addAll(vertex.styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      vertex.selected
    )
  }
}
