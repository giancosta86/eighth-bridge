package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.Includes._
import info.gianlucacosta.helios.fx.geometry.DiagonalBounds
import javafx.beans.property.SimpleDoubleProperty
import scalafx.Includes._
import scalafx.geometry.{BoundingBox, Bounds, Point2D}
import scalafx.scene.Group
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.shape.Rectangle

object DefaultBackgroundNode {
  private val SelectionRectangleMinSize =
    2

  private val EmptySelectionBounds =
    new BoundingBox(0, 0, 0, 0)
}

/**
  * Default, interactive implementation of BackgroundNode
  */
class DefaultBackgroundNode[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
](val graphCanvas: GraphCanvas[V, L, G])
  extends Group
    with BackgroundNode[V, L, G] {

  styleClass.add("graph")

  private var dragAnchor: Point2D = _


  private val selectionBoundsX =
    new SimpleDoubleProperty(0)

  private val selectionBoundsY =
    new SimpleDoubleProperty(0)

  private val selectionBoundsWidth =
    new SimpleDoubleProperty(0)

  private val selectionBoundsHeight =
    new SimpleDoubleProperty(0)


  private def selectionBounds: Bounds =
    new BoundingBox(
      selectionBoundsX(),
      selectionBoundsY(),
      selectionBoundsWidth(),
      selectionBoundsHeight()
    )

  private def selectionBounds_=(newValue: Bounds): Unit = {
    selectionBoundsX() = newValue.minX
    selectionBoundsY() = newValue.minY
    selectionBoundsWidth() = newValue.width
    selectionBoundsHeight() = newValue.height
  }


  protected val backgroundRectangle =
    new Rectangle {
      styleClass.add("backgroundRectangle")
      x = 0
      y = 0

      width <==
        graphCanvas.width

      height <==
        graphCanvas.height
    }

  protected val selectionRectangle =
    new Rectangle {
      styleClass.add("selectionRectangle")

      x <==
        selectionBoundsX

      y <==
        selectionBoundsY

      width <==
        selectionBoundsWidth

      height <==
        selectionBoundsHeight
    }


  children.addAll(
    backgroundRectangle,
    selectionRectangle
  )

  handleEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          dragAnchor =
            mouseEvent.point

          controller.setSelection(Set(), Set()).foreach(graph_=)

        case MouseButton.Secondary =>
          controller.setSelection(Set(), Set()).foreach(graph_=)

        case _ =>
      }

      ()
    }
  }


  handleEvent(MouseEvent.MouseDragged) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          if (controller.canDrawSelectionRectangle) {
            val clippedMousePoint =
              mouseEvent.point.clip(graphCanvas.dimension)

            selectionBounds =
              new DiagonalBounds(dragAnchor, clippedMousePoint)
          }
        case _ =>
      }
    }
  }


  handleEvent(MouseEvent.MouseReleased) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          if (selectionBounds.width < DefaultBackgroundNode.SelectionRectangleMinSize
            && selectionBounds.height < DefaultBackgroundNode.SelectionRectangleMinSize) {
            controller.createVertex(mouseEvent.point)
              .foreach(graph_=)
          } else {
            val selectionVertexes = graphCanvas.vertexNodesByVertex
              .values
              .filter(_.intersects(selectionBounds))
              .map(_.vertexInGraph)
              .toSet

            val selectionLinks = graphCanvas.linkNodesByLink
              .values
              .filter(_.intersects(selectionBounds))
              .map(_.linkInGraph)
              .toSet

            graph =
              graph.setSelection(
                selectionVertexes,
                selectionLinks
              )
          }

          selectionBounds =
            DefaultBackgroundNode.EmptySelectionBounds

        case _ =>
      }
    }
  }
}
