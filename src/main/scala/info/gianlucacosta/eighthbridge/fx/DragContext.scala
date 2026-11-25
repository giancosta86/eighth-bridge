package info.gianlucacosta.eighthbridge.fx

import javafx.beans.property.SimpleBooleanProperty
import scalafx.Includes._
import scalafx.beans.property.ReadOnlyBooleanProperty
import scalafx.scene.Node
import scalafx.scene.input.{MouseButton, MouseEvent}

//TODO! Move this to Helios FX?
trait DragContext extends Node {
  private var startEvent: MouseEvent = _
  private var latestSnapshot: MouseEvent = _

  private val _dragging =
    new SimpleBooleanProperty(false)

  def dragging: ReadOnlyBooleanProperty =
    _dragging

  protected def handleDragStart(event: MouseEvent): Unit = {}

  private def handlePrimaryDragged(event: MouseEvent): Unit = {
    val deltaFromLatest =
      new Delta2D(
        event.screenX - latestSnapshot.screenX,
        event.screenY - latestSnapshot.screenY
      )

    handleDrag(deltaFromLatest, event, startEvent)

    latestSnapshot =
      event
  }

  protected def handleDrag(deltaFromLatest: Delta2D, event: MouseEvent, startEvent: MouseEvent): Unit

  protected def handleDragEnd(event: MouseEvent): Unit = {}

  filterEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 1 =>
              _dragging() =
                true

              startEvent =
                mouseEvent

              latestSnapshot =
                startEvent

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

  filterEvent(MouseEvent.MouseReleased) {
    (event: MouseEvent) => {
      try {
        handleDragEnd(event)
      } finally {
        _dragging() =
          false
      }
    }
  }
}
