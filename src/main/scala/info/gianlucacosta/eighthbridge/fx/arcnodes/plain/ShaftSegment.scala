package info.gianlucacosta.eighthbridge.fx.arcnodes.plain

import info.gianlucacosta.eighthbridge.fx.{Centered, VertexNode}
import info.gianlucacosta.helios.fx.Includes.extendMouseEvent
import scalafx.geometry.Point2D
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.shape.Line
import scalafx.Includes._

private class ShaftSegment(
                            start: Centered,
                            end: Centered,
                            onRightClick: (Point2D) => Unit
                          ) extends Line {
  styleClass.add("line")

  startX <==
    start.centerX

  startY <==
    start.centerY

  endX <==
    end.centerX

  endY <==
    end.centerY

  handleEvent(MouseEvent.MouseClicked) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Secondary =>
          mouseEvent.clickCount match {
            case 1 =>
              onRightClick(mouseEvent.point)
          }

        case _ =>
      }
      ()
    }
  }
}
