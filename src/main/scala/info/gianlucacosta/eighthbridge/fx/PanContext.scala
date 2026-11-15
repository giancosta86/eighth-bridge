package info.gianlucacosta.eighthbridge.fx

import javafx.beans.property.SimpleBooleanProperty
import scalafx.Includes._
import scalafx.beans.property.ReadOnlyBooleanProperty
import scalafx.scene.Node
import scalafx.scene.input.MouseEvent

trait PanContext extends Node with DragContext {
  val panEnabled =
    new SimpleBooleanProperty(true)

  private val _panning =
    new SimpleBooleanProperty(false)

  def panning: ReadOnlyBooleanProperty =
    _panning

  override protected def handleDragStart(event: MouseEvent): Unit = {
    if (event.isShiftDown && panEnabled()) {
      _panning() =
        true

      event.consume()
    }
  }

  override protected def handleDrag(deltaFromLatest: Delta2D, event: MouseEvent, startEvent: MouseEvent): Unit = {
    if (_panning()) {
      event.consume()

      translateX() +=
        deltaFromLatest.x

      translateY() +=
        deltaFromLatest.y
    }
  }

  override protected def handleDragEnd(event: MouseEvent): Unit = {
    if (_panning()) {
      event.consume()

      _panning() =
        false
    }
  }
}
