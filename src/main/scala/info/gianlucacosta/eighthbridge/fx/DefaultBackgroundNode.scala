package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.DefaultBackgroundNode.EmptySelectionBounds
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, NamedVisualVertex, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.Includes._
import info.gianlucacosta.helios.fx.geometry.DiagonalBounds
import javafx.beans.property.{SimpleDoubleProperty, SimpleObjectProperty}
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.geometry.{BoundingBox, Bounds, Point2D}
import scalafx.scene.Group
import scalafx.scene.input.{MouseButton, MouseEvent}
import scalafx.scene.shape.Rectangle

object DefaultBackgroundNode {
  private val SelectionBoundsMinSize =
    2

  private val EmptySelectionBounds =
    new BoundingBox(0, 0, 0, 0)
}

/**
  * Default, interactive implementation of BackgroundNode
  */
class DefaultBackgroundNode(graphCanvas: GraphCanvas)
  extends GraphCanvasNode(graphCanvas) with BackgroundNode with DragContext {

  private val selectionBounds =
    new SimpleObjectProperty[BoundingBox](
      EmptySelectionBounds
    )

  protected val backgroundRectangle =
    new Rectangle {
      styleClass.add("backgroundRectangle")

      x =
        0

      y =
        0

      width <==
        graphCanvas.width

      height <==
        graphCanvas.height
    }

  protected val selectionRectangle =
    new Rectangle {
      styleClass.add("selectionRectangle")

      x <==
        Bindings.createDoubleBinding(
          () =>
            selectionBounds().minX,

          selectionBounds
        )

      y <==
        Bindings.createDoubleBinding(
          () =>
            selectionBounds().minY,

          selectionBounds
        )

      width <==
        Bindings.createDoubleBinding(
          () =>
            selectionBounds().width,

          selectionBounds
        )

      height <==
        Bindings.createDoubleBinding(
          () =>
            selectionBounds().height,

          selectionBounds
        )
    }


  children.addAll(
    backgroundRectangle,
    selectionRectangle
  )

  protected override def handlePrimaryPressed(mouseEvent: MouseEvent): Unit = {
    controller.setSelection(Set(), Set())
  }

  protected override def handleSecondaryPressed(mouseEvent: MouseEvent): Unit = {
    controller.setSelection(Set(), Set())
  }

  protected override def handlePrimaryReleased(mouseEvent: MouseEvent): Unit = {
    if (selectionBounds().width < DefaultBackgroundNode.SelectionBoundsMinSize
      && selectionBounds().height < DefaultBackgroundNode.SelectionBoundsMinSize) {
      tryToCreateNewVertex(mouseEvent.point)
    } else {
      setSelectionFromRectangle()
    }

    selectionBounds() =
      DefaultBackgroundNode.EmptySelectionBounds
  }

  override protected def handleDragMotion(deltaFromLatest: Delta, event: MouseEvent, startEvent: MouseEvent): Unit = {
    if (controller.canDrawSelectingArea) {
      val clippedMousePoint =
        event.point.clip(graphCanvas.dimension())

      selectionBounds() =
        new DiagonalBounds(startEvent.point, clippedMousePoint)
    }
  }

  private def tryToCreateNewVertex(center: Point2D): Unit = {
    controller.addVertex(center)
  }

  private def setSelectionFromRectangle(): Unit = {
    val selectionVertexes = graphCanvas.vertexNodesByVertex
        .values
        .filter(_.intersects(selectionBounds()))
        .map(_.vertex().asInstanceOf[VisualVertex])
        .toSet

      val selectionArcs = graphCanvas.arcNodesByArc
        .values
        .filter(_.intersects(selectionBounds()))
        .map(_.arc().asInstanceOf[VisualLink])
        .toSet

      controller.setSelection(
        selectionVertexes,
        selectionArcs
      )
  }
}
