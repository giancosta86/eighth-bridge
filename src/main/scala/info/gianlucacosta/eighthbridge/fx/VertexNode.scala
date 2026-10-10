package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.{SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._

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
  val vertexInGraph: V

  val vertex: SimpleObjectProperty[V] =
    new SimpleObjectProperty(vertexInGraph)

  vertex.addListener((_: javafx.beans.Observable) => {
    updateFx()
  })

  protected val centerX =
    new SimpleDoubleProperty(vertexInGraph.center.x)

  protected val centerY =
    new SimpleDoubleProperty(vertexInGraph.center.y)

  def width: ReadOnlyDoubleProperty

  def height: ReadOnlyDoubleProperty

  protected[fx] def updateFx(): Unit = {
    styleClass.setAll("vertex")
    styleClass.addAll(vertex().styleClasses)

    centerX() = vertex().center.x
    centerY() = vertex().center.y

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      vertex().selected
    )
  }
}
