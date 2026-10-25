package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{Centered, DragContext}
import info.gianlucacosta.eighthbridge.fx.graph.{ArcHandle, Delta}
import info.gianlucacosta.helios.fx.Includes.{extendNode, extendPoint2D}
import javafx.beans.property.SimpleObjectProperty
import scalafx.scene.shape.Ellipse
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.ObjectProperty
import scalafx.geometry.Point2D
import scalafx.scene.input.{MouseButton, MouseEvent}

private class HandleNode(
                          initialHandle: ArcHandle,
                          onRightClick: HandleNodeRightClickHandler,
                          onDrag: HandleNodeDragHandler
                        ) extends Ellipse with Centered with DragContext {
  styleClass.add("handle")

  //TODO! Choose a style for typing properties
  val handle: ObjectProperty[ArcHandle] =
    new SimpleObjectProperty[ArcHandle](initialHandle)

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
              mouseEvent.consume()
              onRightClick(handle())
          }

        case _ =>
      }
      ()
    }
  }

  override protected def handleDragMotion(deltaFromLatest: Delta, event: MouseEvent, startEvent: MouseEvent): Unit = {
    onDrag(
      handle(),
      deltaFromLatest
    )
  }
}
