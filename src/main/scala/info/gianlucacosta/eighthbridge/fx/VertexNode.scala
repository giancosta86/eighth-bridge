package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.Delta.Point2DDeltaExtensions
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.Includes.extendMouseEvent
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty, SimpleLongProperty, SimpleObjectProperty}
import scalafx.Includes._
import scalafx.beans.binding.Bindings

import scala.collection.JavaConversions._
import scalafx.beans.property.{ReadOnlyBooleanProperty, ReadOnlyDoubleProperty, ReadOnlyDoubleWrapper}
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.layout.Region

/**
  * JavaFX node rendering a VisualVertex
  */
abstract class VertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends GraphCanvasNode(graphCanvas) with Centered with DragContext {

  val vertex =
    new SimpleObjectProperty[VisualVertex](initialVertex)

  protected val timestamp =
    new SimpleLongProperty

  timestamp.onChange {
    println(s"TIMESTAMP FOR VERTEX ${vertex()} IS: ${timestamp}")
    styleClass.setAll("vertex")
    styleClass.addAll(vertex().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      vertex().selected
    )
  }

  timestamp <==
    Bindings.createLongBinding(
      () =>
        vertex().timestamp,

      vertex
    )

  val centerX: ReadOnlyDoubleProperty = {
    val wrapper =
      new ReadOnlyDoubleWrapper()

    wrapper.bind(
      Bindings.createDoubleBinding(
        () =>
          vertex().center.x,

        vertex
      )
    )

    wrapper
  }

  val centerY: ReadOnlyDoubleProperty = {
    val wrapper =
      new ReadOnlyDoubleWrapper()

    wrapper.bind(
      Bindings.createDoubleBinding(
        () =>
          vertex().center.y,

        vertex
      )
    )

    wrapper
  }

  def width: ReadOnlyDoubleProperty
  def height: ReadOnlyDoubleProperty

  val selected =
    Bindings.createBooleanBinding(
      () => vertex().selected,

      vertex
    )

  protected override def handleSinglePrimaryPressed(point: Point2D, controlPressed: Boolean): Unit = {
    val controllerResult =
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
      } else {
        if (selected()) {
          None
        } else {
          controller.setSelection(
            selectedVertexes =
              Set(vertex()),

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

      controller.createArc(selectedVertex, vertex())
        .foreach(newGraph =>
          graph =
            newGraph
        )
    }
  }

  override protected def handleDragMotion(delta: Delta, point: Point2D, startPoint: Point2D): Unit = {
    if (vertex().selected) {
      controller
        .dragSelectedVertexesBy(delta)
        .foreach(graph_=)
    }
  }
}
