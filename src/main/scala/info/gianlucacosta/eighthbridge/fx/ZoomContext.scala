package info.gianlucacosta.eighthbridge.fx

import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty}
import scalafx.scene.Node
import scalafx.scene.input.ScrollEvent
import scalafx.Includes._

trait ZoomContext extends Node {
  val zoomEnabled =
    new SimpleBooleanProperty(true)

  val minZoomScale =
    new SimpleDoubleProperty(0.2)

  val maxZoomScale =
    new SimpleDoubleProperty(Double.PositiveInfinity)

  filterEvent(ScrollEvent.Scroll) {
    (event: ScrollEvent) => {
      handleMouseScroll(event)
    }
  }

  private def handleMouseScroll(event: ScrollEvent): Unit = {
    if (zoomEnabled()) {
      event.consume()

      val oldScale =
        scaleX()

      val scaleFactor =
        math.pow(1.01, event.deltaY / 5)

      val newScale = math.max(
        minZoomScale(),

        math.min(
          maxZoomScale(),

          oldScale * scaleFactor
        )
      )

      scaleX() =
        newScale

      scaleY() =
        newScale

      val zoomFactor =
        (newScale / oldScale) - 1

      val canvasBounds =
        this.getBoundsInParent

      val deltaX =
        event.sceneX - (canvasBounds.width / 2 + canvasBounds.minX)

      val deltaY =
        event.sceneY - (canvasBounds.height / 2 + canvasBounds.minY)

      translateX() -=
        zoomFactor * deltaX

      translateY() -=
        zoomFactor * deltaY
    }
  }
}
