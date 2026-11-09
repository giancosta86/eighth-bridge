package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.beans.property.ReadOnlyDoubleProperty
import scalafx.geometry.{Dimension2D, Point2D}
import scalafx.scene.input.MouseEvent

import scala.collection.JavaConversions._

/**
  * JavaFX node rendering a VisualVertex
  */
abstract class VertexNode(graphCanvas: GraphCanvas, initialVertex: VisualVertex)
  extends GraphCanvasNode(graphCanvas) with DragContext {

  type V <:
    VisualVertex

  val vertex =
    new SimpleObjectProperty[V]

  vertex.onInvalidate {
    styleClass.setAll(
      "entity",
      "vertex"
    )
    styleClass.addAll(vertex().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      vertex().selected
    )
  }

  vertex() =
    initialVertex.asInstanceOf[V]

  def width: ReadOnlyDoubleProperty
  def height: ReadOnlyDoubleProperty

  protected override def handlePrimaryPressed(mouseEvent: MouseEvent): Unit = {
    if (mouseEvent.controlDown) {
      val vertexesToSelect =
        if (vertex().selected)
          graph.selectedVertexes - vertex()
        else
          graph.selectedVertexes + vertex()

      controller.setSelection(
        selectedVertexes =
          vertexesToSelect,

        selectedLinks =
          graph.selectedLinks
      )
    } else if (!vertex().selected) {
      controller.setSelection(
        selectedVertexes =
          Set(vertex()),

        selectedLinks =
          Set()
      )
    }
  }

  protected override def handlePrimaryDoublePressed(mouseEvent: MouseEvent): Unit = {
    controller.setSelection(
      selectedVertexes = Set(vertex()),
      selectedLinks = Set()
    )

    controller.editVertex(vertex())
  }

  protected override def handleSecondaryPressed(mouseEvent: MouseEvent): Unit = {
    val canStartCreatingLink =
      graph.selectedVertexes.size == 1 && !graph.selectedVertexes.contains(vertex())

    if (canStartCreatingLink) {
      val selectedVertex =
        graph.selectedVertexes.head

      controller.addLink(selectedVertex, vertex())
    }
  }

  override protected def handleDragMotion(deltaFromLatest: Delta, event: MouseEvent, startEvent: MouseEvent): Unit = {
    controller
      .dragSelectedVertexes(deltaFromLatest)
  }

  def getIntersectionWithSegmentFromPoint(sourcePoint: Point2D): Option[Point2D] =
    Segments.getIntersectionWithRectangle(
      sourcePoint,
      vertex().center,
      new Dimension2D(
        width(),
        height()
      )
    )
}
