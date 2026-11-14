package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.DragContext
import info.gianlucacosta.eighthbridge.fx.graph.Delta
import info.gianlucacosta.eighthbridge.graphs.facets.LinkHandle
import info.gianlucacosta.helios.fx.Includes.extendNode
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.shape.Ellipse

private class HandleNode(
                          initialHandle: LinkHandle,
                          onRightClick: HandleNodeRightClickHandler,
                          onDrag: HandleNodeDragHandler
                        ) extends Ellipse with DragContext {
  styleClass.add("handle")

  val handle: ObjectProperty[LinkHandle] =
    new SimpleObjectProperty[LinkHandle](initialHandle)

  radiusX <==
    Bindings.createDoubleBinding(
      () =>
        handle().dimension.width / 2,

      handle
    )

  radiusY <==
    Bindings.createDoubleBinding(
      () =>
        handle().dimension.height / 2,

      handle
    )

  centerX <==
    Bindings.createDoubleBinding(
      () =>
        handle().center.x,

      handle
    )

  centerY <==
    Bindings.createDoubleBinding(
      () =>
        handle().center.y,

      handle
    )

  this.consumeMouseEvents()

  handleEvent(MouseEvent.MouseClicked) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 1 =>
              onRightClick(handle())

            case _ =>
          }

        case _ =>
      }
    }
  }

  override protected def handleDragMotion(deltaFromLatest: Delta, event: MouseEvent, startEvent: MouseEvent): Unit = {
    onDrag(
      handle(),
      deltaFromLatest
    )
  }
}
