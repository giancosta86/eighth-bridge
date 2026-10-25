package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{ArcBarbs, VisualLink, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.{SimpleObjectProperty}

import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafx.beans.binding.Bindings
import scalafx.beans.property.{ObjectProperty, ReadOnlyObjectProperty}
import scalafx.geometry.Point2D
import scalafx.scene.Group
import scalafx.scene.input.MouseEvent

/**
  * JavaFX node rendering a VisualArc
  */
abstract class ArcNode(graphCanvas: GraphCanvas, initialArc: VisualLink)
  extends GraphCanvasNode(graphCanvas) {

  val arc =
    new SimpleObjectProperty[VisualLink]

  arc.onInvalidate {
    styleClass.setAll("arc")
    styleClass.addAll(arc().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      arc().selected
    )
  }

  arc() =
    initialArc

  //TODO! Remove all println() calls!

  protected override def handleSinglePrimaryPressed(point: Point2D, controlPressed: Boolean): Unit = {
    val controllerResult =
      if (controlPressed) {
        val arcsToSelect =
          if (arc().selected)
            graph.selectedArcs - arc()
          else
            graph.selectedArcs + arc()

        controller.setSelection(
          selectedVertexes =
            graph.selectedVertexes,

          selectedArcs =
            arcsToSelect
        )
      } else {
        if (arc().selected) {
          None
        } else {
          controller.setSelection(
            selectedVertexes =
              Set(),

            selectedArcs =
              Set(arc())
          )
        }
      }

    controllerResult.foreach(graph_=)
  }
}
