package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{ArcBarbs, VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty

import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafx.beans.property.{ObjectProperty, ReadOnlyObjectProperty}
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.scene.input.MouseEvent

/**
  * JavaFX node rendering a VisualArc
  */
abstract class ArcNode(graphCanvas: GraphCanvas, initialArc: VisualArc)
  extends GraphCanvasNode(graphCanvas) {
  private var _arc: VisualArc =
    initialArc

  private val _arcBarbs =
    new SimpleObjectProperty[Option[ArcBarbs]](None)

  protected val arcBarbs: ReadOnlyObjectProperty[Option[ArcBarbs]] =
    _arcBarbs

  def arc: VisualArc =
    _arc

  protected[fx] def update(updatedArc: VisualArc): Unit = {
    //TODO! Remove all println() calls!
    _arc =
      updatedArc

    _arcBarbs() =
      updatedArc.barbs

    styleClass.setAll("arc")
    styleClass.addAll(updatedArc.styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      updatedArc.selected
    )
  }

  protected override def handleSinglePrimaryPressed(point: Point2D, controlPressed: Boolean): Unit = {
    val controllerResult =
      if (controlPressed) {
        val arcsToSelect =
          if (arc.selected)
            graph.selectedArcs - arc
          else
            graph.selectedArcs + arc

        controller.setSelection(
          selectedVertexes =
            graph.selectedVertexes,

          selectedArcs =
            arcsToSelect
        )
      } else {
        if (arc.selected) {
          None
        } else {
          controller.setSelection(
            selectedVertexes =
              Set(),

            selectedArcs =
              Set(arc)
          )
        }
      }

    controllerResult.foreach(graph_=)
  }
}
