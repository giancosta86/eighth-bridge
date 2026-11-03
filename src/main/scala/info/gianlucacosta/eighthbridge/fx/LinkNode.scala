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
abstract class LinkNode(graphCanvas: GraphCanvas, initialArc: VisualLink)
  extends GraphCanvasNode(graphCanvas) {

  type L <:
    VisualLink

  val link =
    new SimpleObjectProperty[L]

  link.onInvalidate {
    styleClass.setAll(
      "entity",
      "link"
    )
    styleClass.addAll(link().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      link().selected
    )
  }

  link() =
    initialArc.asInstanceOf[L]

  //TODO! Remove all println() calls!

  protected override def handlePrimaryPressed(mouseEvent: MouseEvent): Unit = {
    if (mouseEvent.controlDown) {
      val arcsToSelect =
        if (link().selected)
          graph.selectedArcs - link()
        else
          graph.selectedArcs + link()

      controller.setSelection(
        selectedVertexes =
          graph.selectedVertexes,

        selectedLinks =
          arcsToSelect
      )
    } else if (!link().selected) {
      controller.setSelection(
        selectedVertexes =
          Set(),

        selectedLinks =
          Set(link())
      )
    }
  }
}
