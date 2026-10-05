package info.gianlucacosta.eighthbridge.fx.canvas

import info.gianlucacosta.helios.fx.css.PseudoClasses

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


  private[canvas] def vertex_=(newVertex: V): Unit =
    _vertex = newVertex


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
