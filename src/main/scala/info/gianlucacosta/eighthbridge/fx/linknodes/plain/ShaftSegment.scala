package info.gianlucacosta.eighthbridge.fx.linknodes.plain

import info.gianlucacosta.eighthbridge.fx.{VertexNode}
import info.gianlucacosta.helios.fx.Includes.extendMouseEvent
import scalafx.geometry.Point2D
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.shape.Line
import scalafx.Includes._

private class ShaftSegment(
                            start: Point2D,
                            end: Point2D,
                            onRightClick: (Point2D) => Unit
                          ) extends Line {
  styleClass.add("line")

  startX <==
    start.x

  startY <==
    start.y

  endX <==
    end.x

  endY <==
    end.y

  handleEvent(MouseEvent.MouseClicked) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 1 =>
              onRightClick(mouseEvent.point)

            case _ =>
          }

        case _ =>
      }
    }
  }
}
