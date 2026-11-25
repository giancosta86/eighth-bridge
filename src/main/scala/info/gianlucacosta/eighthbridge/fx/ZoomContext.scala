package info.gianlucacosta.eighthbridge.fx

import javafx.beans.property.{SimpleBooleanProperty, SimpleDoubleProperty}
import scalafx.Includes._
import scalafx.scene.Node
import scalafx.scene.input.ScrollEvent

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

      val currentScale =
        scaleX()

      val scaleFactor =
        math.pow(1.01, event.deltaY / 5)

      val newScale = math.max(
        minZoomScale(),

        math.min(
          maxZoomScale(),

          currentScale * scaleFactor
        )
      )

      scaleX() =
        newScale

      scaleY() =
        newScale

      val zoomFactor =
        (newScale / currentScale) - 1

      val currentBounds =
        this.getBoundsInParent

      val deltaX =
        event.sceneX - (currentBounds.width / 2 + currentBounds.minX)

      val deltaY =
        event.sceneY - (currentBounds.height / 2 + currentBounds.minY)

      translateX() -=
        zoomFactor * deltaX

      translateY() -=
        zoomFactor * deltaY
    }
  }
}
