package info.gianlucacosta.eighthbridge.fx.linknodes.labeled

import info.gianlucacosta.eighthbridge.fx.{Delta2D, DragContext}
import info.gianlucacosta.helios.fx.Includes.extendNode
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.control.Label
import scalafx.scene.input.{MouseButton, MouseEvent}

private class LinkLabel(
                         initialCenter: Point2D,
                         onDrag: LabelDragHandler,
                         onSecondaryDoubleClick: LabelSecondaryDoubleClickHandler
                       )
  extends Label with DragContext {
  styleClass.add("label")

  val center: ObjectProperty[Point2D] =
    new SimpleObjectProperty[Point2D](initialCenter)

  layoutX <==
    Bindings.createDoubleBinding(
      () =>
        center().x - width() / 2,

      center,
      width
    )

  layoutY <==
    Bindings.createDoubleBinding(
      () =>
        center().y - height() / 2,

      center,
      height
    )

  def bottomRight: Point2D =
    new Point2D(
      center().x + width() / 2,
      center().y + height() / 2
    )

  this.consumeMouseEvents()

  override protected def handleDrag(deltaFromLatest: Delta2D, event: MouseEvent, startEvent: MouseEvent): Unit = {
    onDrag(deltaFromLatest)
  }

  handleEvent(MouseEvent.MouseClicked) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 2 =>
              onSecondaryDoubleClick()

            case _ =>
          }

        case _ =>
      }
    }
  }
}

