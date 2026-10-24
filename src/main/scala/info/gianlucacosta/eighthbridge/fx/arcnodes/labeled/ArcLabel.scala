package info.gianlucacosta.eighthbridge.fx.arcnodes.labeled

import info.gianlucacosta.eighthbridge.fx.DragContext
import info.gianlucacosta.eighthbridge.fx.graph.Delta
import javafx.beans.property.{SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.beans.property.{DoubleProperty, ObjectProperty}
import scalafx.scene.control.Label
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.Point2D
import scalafx.scene.input.{MouseButton, MouseEvent}

private class ArcLabel(
                        onDrag: LabelDragHandler,
                        onDoubleClick: LabelDoubleClickhandler
                      )
  extends Label with DragContext {
  styleClass.add("label")

  val center: ObjectProperty[Point2D] =
    new SimpleObjectProperty[Point2D]

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

  //TODO! Use .consumeMouseEvents!
  handleEvent(MouseEvent.Any) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.consume()
    }
  }

  override protected def handleDragMotion(delta: Delta, point: Point2D, startPoint: Point2D): Unit = {
    onDrag(point)
  }

  handleEvent(MouseEvent.MouseClicked) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          mouseEvent.clickCount match {
            case 2 =>
              onDoubleClick()

            case _ =>
          }

        case _ =>
      }
    }
  }


}

