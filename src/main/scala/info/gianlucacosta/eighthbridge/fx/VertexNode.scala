package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.Delta.Point2DDeltaExtensions
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.Includes.extendMouseEvent
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._

import scala.collection.JavaConversions._
import scalafx.beans.property.{ReadOnlyBooleanProperty, ReadOnlyDoubleProperty}
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.layout.Region

/**
  * JavaFX node rendering a VisualVertex
  */
abstract class VertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends GraphCanvasNode(graphCanvas) with Centered with DragContext {

  private var _vertex: VisualVertex =
    initialVertex

  def vertex: VisualVertex =
    _vertex

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
    new SimpleBooleanProperty()

  val selected: ReadOnlyBooleanProperty =
    _selected

  protected[fx] def update(updatedVertex: VisualVertex): Unit = {
    _vertex =
      updatedVertex

    _centerX() =
      updatedVertex.center.x

    _centerY() =
      updatedVertex.center.y

    _selected() =
      updatedVertex.selected

    styleClass.setAll("vertex")
    styleClass.addAll(updatedVertex.styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      updatedVertex.selected
    )
  }

  protected override def handleSinglePrimaryPressed(point: Point2D, controlPressed: Boolean): Unit = {
    val controllerResult =
      if (controlPressed) {
        val vertexesToSelect =
          if (vertex.selected)
            graph.selectedVertexes - vertex
          else
            graph.selectedVertexes + vertex

        controller.setSelection(
          selectedVertexes =
            vertexesToSelect,

          selectedArcs =
            graph.selectedArcs
        )
      } else {
        if (vertex.selected) {
          None
        } else {
          controller.setSelection(
            selectedVertexes =
              Set(vertex),

            selectedArcs =
              Set()
          )
        }
      }


    controllerResult.foreach(graph_=)
  }

  protected override def handleDoublePrimaryPressed(point: Point2D): Unit = {
    val selectedVertexes =
      graph.selectedVertexes

    if (selectedVertexes.size == 1 && graph.selectedArcs.isEmpty) {
      val selectedVertex =
        selectedVertexes.head

      controller.editVertex(selectedVertex)
        .foreach(newGraph =>
          graph =
            newGraph
        )
    }
  }


  protected override def handleSingleSecondaryPressed(point: Point2D): Unit = {
    if (graph.selectedVertexes.size == 1 && !graph.selectedVertexes.contains(initialVertex) && graph.selectedArcs.isEmpty) {
      val selectedVertex =
        graph.selectedVertexes.head

      controller.createArc(selectedVertex, _vertex)
        .foreach(newGraph =>
          graph =
            newGraph
        )
    }
  }

  override protected def handleDragMotion(delta: Delta, point: Point2D, startPoint: Point2D): Unit = {
    if (vertex.selected) {
      controller
        .dragSelectedVertexesBy(delta)
        .foreach(graph_=)
    }
  }
}
