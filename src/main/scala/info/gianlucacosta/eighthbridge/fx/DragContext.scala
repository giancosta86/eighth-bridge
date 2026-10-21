package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.Delta
import info.gianlucacosta.eighthbridge.fx.graph.Delta.Point2DDeltaExtensions
import info.gianlucacosta.helios.fx.Includes.extendMouseEvent
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.Node
import scalafx.scene.input.{MouseButton, MouseEvent}

//TODO! Move this to Helios FX?
trait DragContext extends Node {
  private var startPoint: Point2D = _
  private var latestSnapshot: Point2D = _

  protected def handleDragMotion(delta: Delta, point: Point2D, startPoint: Point2D): Unit

  private def handleSinglePrimaryPressed(point: Point2D): Unit = {
    startPoint =
      point

    latestSnapshot =
      startPoint
  }

  private def handlePrimaryDragged(point: Point2D): Unit = {
      handleDragMotion(point - latestSnapshot, point, startPoint)

      latestSnapshot =
        point
  }

  handleEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 1 =>
              handleSinglePrimaryPressed(mouseEvent.point)

            case _ =>
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
