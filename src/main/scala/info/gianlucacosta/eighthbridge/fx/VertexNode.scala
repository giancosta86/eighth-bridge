package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.Delta.Point2DDeltaExtensions
import info.gianlucacosta.eighthbridge.fx.graph.{VisualArc, VisualGraph, VisualVertex}
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
  extends GraphCanvasNode(graphCanvas) {

  toFront()

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

  private var dragAnchor: Point2D = _

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

  private def handleSinglePrimaryPressed(point: Point2D, controlDown: Boolean): Unit = {
    dragAnchor =
      point

    if (controller.canSelect) {
      if (controlDown) {
        graph =
          graph.updateVertex(
            vertex.setSelected(!vertex.selected)
          )
      } else if (!initialVertex.selected) {
        graph =
          graph.setSelection(Set(vertex), Set())
      }
    }
  }


  private def handlePrimaryDragged(mousePoint: Point2D): Unit = {
    if (vertex.selected && controller.canDragVertexes) {
      val delta =
        mousePoint - dragAnchor

      graph =
        graph.moveSelectedVertexesBy(delta)

      dragAnchor =
        mousePoint
    }
  }


  handleEvent(MouseEvent.Any) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.consume()
    }
  }


  handleEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 1 =>
              handleSinglePrimaryPressed(mouseEvent.point, controlDown = mouseEvent.controlDown)

            case 2 =>
              /*val selectedVertexes =
                graph.selectedVertexes

              if (selectedVertexes.size == 1 && graph.selectedArcs.isEmpty) {
                val selectedVertex =
                  selectedVertexes.head

                controller.editVertex(selectedVertex)
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              }*/

            case _ =>
          }

        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 1 =>
              /*if (graph.selectedVertexes.size == 1 && !graph.selectedVertexes.contains(initialVertex) && graph.selectedArcs.isEmpty) {
                val selectedVertex =
                  graph.selectedVertexes.head

                controller.createArc(selectedVertex, initialVertex)
                  .foreach(newGraph =>
                    graph =
                      newGraph
                  )
              }*/
          }

        case _ =>
      }
      ()
    }
  }


  handleEvent(MouseEvent.MouseDragged) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          handlePrimaryDragged(mouseEvent.point)

        case _ =>
      }
    }
  }
}
