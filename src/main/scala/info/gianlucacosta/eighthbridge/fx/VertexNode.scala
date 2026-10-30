package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.Delta.Point2DDeltaExtensions
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualLink, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.Includes.extendMouseEvent
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._
import scalafx.beans.binding.Bindings

import scala.collection.JavaConversions._
import scalafx.beans.property.{ReadOnlyBooleanProperty, ReadOnlyDoubleProperty, ReadOnlyDoubleWrapper, ReadOnlyObjectProperty}
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.layout.Region

/**
  * JavaFX node rendering a VisualVertex
  */
abstract class VertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends GraphCanvasNode(graphCanvas) with Centered with DragContext {

  type V <:
    VisualVertex

  val vertex =
    new SimpleObjectProperty[V]

  vertex.onInvalidate {
    styleClass.setAll("vertex")
    styleClass.addAll(vertex().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      vertex().selected
    )
  }

  vertex() =
    initialVertex.asInstanceOf[V]

  val centerX: ReadOnlyDoubleProperty =
    ReadOnlyProperties.createDouble(
      () =>
        vertex().center.x,

      vertex
    )

  val centerY: ReadOnlyDoubleProperty =
    ReadOnlyProperties.createDouble(
      () =>
        vertex().center.y,

      vertex
    )

  val center: ReadOnlyObjectProperty[Point2D] =
    ReadOnlyProperties.createObject[Point2D](
      () =>
        new Point2D(
          centerX(),
          centerY()
        ),

      centerX,
      centerY
    )

  def width: ReadOnlyDoubleProperty
  def height: ReadOnlyDoubleProperty

  private val selected =
    Bindings.createBooleanBinding(
      () => vertex().selected,

      vertex
    )

  protected override def handleSinglePrimaryPressed(point: Point2D, controlPressed: Boolean): Unit = {
    if (controlPressed) {
      val vertexesToSelect =
        if (selected())
          graph.selectedVertexes - vertex()
        else
          graph.selectedVertexes + vertex()

      controller.setSelection(
        selectedVertexes =
          vertexesToSelect,

        selectedArcs =
          graph.selectedArcs
      )
    } else if (selected()) {
      controller.setSelection(
        selectedVertexes =
          Set(vertex()),

        selectedArcs =
          Set()
      )
    }
  }

  protected override def handleDoublePrimaryPressed(point: Point2D): Unit = {
    val selectedVertexes =
      graph.selectedVertexes

    if (selectedVertexes.size == 1 && graph.selectedArcs.isEmpty) {
      val selectedVertex =
        selectedVertexes.head

      controller.editVertex(selectedVertex)
    }
  }


  protected override def handleSingleSecondaryPressed(point: Point2D): Unit = {
    if (graph.selectedVertexes.size == 1 && !graph.selectedVertexes.contains(initialVertex) && graph.selectedArcs.isEmpty) {
      val selectedVertex =
        graph.selectedVertexes.head

      controller.createLink(selectedVertex, vertex())
    }
  }

  override protected def handleDragMotion(deltaFromLatest: Delta, event: MouseEvent, startEvent: MouseEvent): Unit = {
    if (vertex().selected) {
      controller
        .dragSelectedVertexes(deltaFromLatest)
    }
  }
}
