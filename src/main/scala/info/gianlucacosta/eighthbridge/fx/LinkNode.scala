package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.VisualLink
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.scene.input.MouseEvent

import scala.collection.JavaConversions._

/**
  * JavaFX node rendering a VisualLink
  */
abstract class LinkNode(graphCanvas: GraphCanvas, initialLink: VisualLink)
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
    initialLink.asInstanceOf[L]

  //TODO! Remove all println() calls!

  protected override def handlePrimaryPressed(mouseEvent: MouseEvent): Unit = {
    if (mouseEvent.controlDown) {
      val linksToSelect =
        if (link().selected)
          graph.selectedLinks - link()
        else
          graph.selectedLinks + link()

      controller.setSelection(
        selectedVertexes =
          graph.selectedVertexes,

        selectedLinks =
          linksToSelect
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
