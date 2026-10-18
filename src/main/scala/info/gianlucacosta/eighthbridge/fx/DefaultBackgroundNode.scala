package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{NamedVisualVertex, VisualArc, VisualGraph, VisualVertex}
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
class DefaultBackgroundNode(graphCanvas: GraphCanvas)
  extends GraphCanvasNode(graphCanvas) with BackgroundNode {
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

  private def handleSinglePrimaryPressed(point: Point2D): Unit = {
    dragAnchor =
      point

    controller.setSelection(Set(), Set()).foreach(graph_=)
  }

  private def handleSingleSecondaryPressed(): Unit = {
    controller.setSelection(Set(), Set()).foreach(graph_=)
  }

  private def handleSinglePrimaryReleased(point: Point2D): Unit = {
    if (selectionBounds.width < DefaultBackgroundNode.SelectionRectangleMinSize
      && selectionBounds.height < DefaultBackgroundNode.SelectionRectangleMinSize) {
      tryToCreateNewVertex(point)
    } else {
      setSelectionFromRectangle()
    }

    selectionBounds =
      DefaultBackgroundNode.EmptySelectionBounds
  }

  private def handlePrimaryDragged(point: Point2D): Unit = {
    if (controller.canDrawSelectionRectangle) {
      val clippedMousePoint =
        point.clip(graphCanvas.dimension)

      if (controller.canSelect) {
        selectionBounds =
          new DiagonalBounds(dragAnchor, clippedMousePoint)
      }
    }
  }

  private def tryToCreateNewVertex(center: Point2D): Unit = {
    //TODO! controller.createVertex(center)
    Some(new NamedVisualVertex(center, "Dodo"))
      .foreach(vertex =>
        graph = graph.addVertex(vertex)
      )
  }

  private def setSelectionFromRectangle(): Unit = {
    val selectionVertexes = graphCanvas.vertexNodesByVertex
        .values
        .filter(_.intersects(selectionBounds))
        .map(_.vertex)
        .toSet

      val selectionArcs = graphCanvas.arcNodesByArc
        .values
        .filter(_.intersects(selectionBounds))
        .map(_.arc)
        .toSet

      graph =
        graph.setSelection(
          selectionVertexes,
          selectionArcs
        )
  }

  handleEvent(MouseEvent.MousePressed) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          handleSinglePrimaryPressed(mouseEvent.point)

        case MouseButton.Secondary =>
          handleSingleSecondaryPressed()

        case _ =>
      }

      ()
    }
  }


  handleEvent(MouseEvent.MouseDragged) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          handlePrimaryDragged(mouseEvent.point)
        case _ =>
      }
    }
  }


  handleEvent(MouseEvent.MouseReleased) {
    (mouseEvent: MouseEvent) => {
      mouseEvent.button match {
        case MouseButton.Primary =>
          handleSinglePrimaryReleased(mouseEvent.point)

        case _ =>
      }
    }
  }

}
