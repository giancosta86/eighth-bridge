package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualLink, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.Includes.{extendMouseEvent, extendNode}
import scalafx.scene.{Group, Node}
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.input.{MouseButton, MouseEvent}

/**
  * JavaFX node rendering a graph element into GraphCanvas.
  */
abstract class GraphCanvasNode(protected val graphCanvas: GraphCanvas) extends Group {
  /**
    * The controller of the owning graph canvas
    *
    * @return
    */
  protected final def controller: GraphCanvasController =
    graphCanvas.controller

  /**
    * The current graph within the graph canvas
    *
    * @return
    */
  protected final def graph: VisualGraph =
    graphCanvas.graph()

  this.consumeMouseEvents()

  protected def handlePrimaryPressed(mouseEvent: MouseEvent): Unit = { }

  protected def handlePrimaryDoublePressed(mouseEvent: MouseEvent): Unit = { }

  protected def handleSecondaryPressed(mouseEvent: MouseEvent): Unit = { }

  protected def handlePrimaryReleased(mouseEvent: MouseEvent): Unit = { }

  //TODO! Is this still recommended?
  handleEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 1 =>
              handlePrimaryPressed(mouseEvent)

            case 2 =>
              handlePrimaryDoublePressed(mouseEvent)

            case _ =>
          }

        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 1 =>
              handleSecondaryPressed(mouseEvent)
          }

        case _ =>
      }
      ()
    }
  }

  handleEvent(MouseEvent.MouseReleased) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          handlePrimaryReleased(mouseEvent)

        case _ =>
      }
    }
  }
}
