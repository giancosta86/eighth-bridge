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
  private var startEvent: MouseEvent = _
  private var latestSnapshot: MouseEvent = _

  protected def handleDragStart(event: MouseEvent): Unit = {
    startEvent =
      event

    latestSnapshot =
      startEvent
  }

  private def handlePrimaryDragged(event: MouseEvent): Unit = {
      val deltaFromLatest =
        new Delta(
          event.screenX - latestSnapshot.screenX,
          event.screenY - latestSnapshot.screenY
        )

      handleDragMotion(deltaFromLatest, event, startEvent)

      latestSnapshot =
        event
  }

  protected def handleDragMotion(deltaFromLatest: Delta, event: MouseEvent, startEvent: MouseEvent): Unit

  protected def handleDragEnd(event: MouseEvent): Unit = { }

  filterEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 1 =>
              handleDragStart(mouseEvent)

            case _ =>
          }

        case _ =>
      }
    }
  }

  filterEvent(MouseEvent.MouseDragged) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          handlePrimaryDragged(mouseEvent)

        case _ =>
      }
    }
  }

  //TODO! Use filterEvent() whenever required, in lieu of handleEvent()!
  filterEvent(MouseEvent.MouseReleased) {
    (event: MouseEvent) => {
      handleDragEnd(event)
    }
  }
}
