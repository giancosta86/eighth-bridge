package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.core.Facet
import info.gianlucacosta.eighthbridge.core.bottomright.HasBottomRight
import info.gianlucacosta.eighthbridge.core.facets.{HasSelected, HasStyleClasses}
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
  extends GraphCanvasNode(graphCanvas) with DragContext with HasBottomRight {

  type V <:
    VisualVertex

  val vertex =
    new SimpleObjectProperty[V]

  vertex.onInvalidate {
    styleClass.setAll(
      "entity",
      "vertex"
    )

    Facet
      .as[HasStyleClasses](vertex())
      .map(_.styleClasses)
      .foreach(styleClass.addAll(_))

    val selected =
      Facet
        .as[HasSelected](vertex())
        .exists(_.selected)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      selected
    )
  }

  vertex() =
    initialVertex.asInstanceOf[V]

  val width: ReadOnlyDoubleProperty
  val height: ReadOnlyDoubleProperty

  protected override def handlePrimaryPressed(mouseEvent: MouseEvent): Unit = {
    Facet
      .as[HasSelected](vertex())
      .map(_.selected)
      .foreach(selected => {
        if (mouseEvent.controlDown) {
          val vertexesToSelect =
            if (selected)
              graph.selectedVertexes - vertex()
            else
              graph.selectedVertexes + vertex()

          controller.setSelection(
            selectedVertexes =
              vertexesToSelect,

            selectedLinks =
              graph.selectedLinks
          )
        } else if (!selected) {
          controller.setSelection(
            selectedVertexes =
              Set(vertex()),

            selectedLinks =
              Set()
          )
        }
      })
  }

  protected override def handlePrimaryDoublePressed(mouseEvent: MouseEvent): Unit = {
    controller.editVertex(vertex())
  }

  protected override def handleSecondaryPressed(mouseEvent: MouseEvent): Unit = {
    val canCreateLink =
      graph.selectedVertexes.size == 1 && !graph.selectedVertexes.contains(vertex())

    if (canCreateLink) {
      val selectedVertex =
        graph.selectedVertexes.head

      controller.addLink(selectedVertex, vertex())
    }
  }

  override protected def handleDrag(deltaFromLatest: Delta2D, event: MouseEvent, startEvent: MouseEvent): Unit = {
    Facet
      .as[HasSelected](vertex())
      .foreach(withSelected => {
        if (withSelected.selected) {
          controller
            .dragSelectedVertexes(deltaFromLatest)
        }
      })
  }

  protected def borderRadius: Double

  def getIntersectionWithSegmentFromPoint(sourcePoint: Point2D): Option[Point2D] =
    Segments.getIntersectionWithRoundedRectangle(
      sourcePoint =
        sourcePoint,
      targetCenter =
        vertex().center,
      targetSize =
        new Dimension2D(
          width(),
          height()
        ),
      borderRadius =
        borderRadius
    )

  override def bottomRight: Point2D =
    new Point2D(
      vertex().center.x + width() / 2,
      vertex().center.y + height() / 2
    )
}
