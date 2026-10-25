package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualLink, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.Includes.{extendMouseEvent, extendNode}
import scalafx.scene.{Group, Node}
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.input.{MouseButton, MouseEvent}

/**
  * Generic JavaFX node rendering a graph element into GraphCanvas.
  */
abstract class GraphCanvasNode(protected val graphCanvas: GraphCanvas) extends Group {
  private var _graphCanvas: GraphCanvas = _

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

  /**
    * Simple way to update the graph contained the graph canvas - thus triggering the rendering process.
    * When migrating from older versions of EighthBridge, use this in lieu of notifyGraphChanged()
    */
  protected final def graph_=(newGraph: VisualGraph): Unit =
    graphCanvas.graph() = newGraph


  this.consumeMouseEvents()

  protected def handleSinglePrimaryPressed(point: Point2D, controlPressed: Boolean): Unit = { }

  protected def handleDoublePrimaryPressed(point: Point2D): Unit = { }

  protected def handleSingleSecondaryPressed(point: Point2D): Unit = { }

  protected def handleSinglePrimaryReleased(point: Point2D): Unit = { }


  handleEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 1 =>
              handleSinglePrimaryPressed(mouseEvent.point, controlPressed = mouseEvent.controlDown)

            case 2 =>
              handleDoublePrimaryPressed(mouseEvent.point)

            case _ =>
          }

        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 1 =>
              handleSingleSecondaryPressed(mouseEvent.point)
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
          handleSinglePrimaryReleased(mouseEvent.point)

        case _ =>
      }
    }
  }
}
