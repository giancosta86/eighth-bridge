package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.core.Facet
import info.gianlucacosta.eighthbridge.core.facets.{HasSelected, HasStyleClasses}
import info.gianlucacosta.eighthbridge.fx.bottomright.HasBottomRight
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty
import scalafx.Includes._
import scalafx.geometry.Point2D
import scalafx.scene.input.MouseEvent

import scala.collection.JavaConversions._

/**
 * JavaFX node rendering a VisualLink
 */
abstract class LinkNode(graphCanvas: GraphCanvas, initialLink: VisualLink)
  extends GraphCanvasNode(graphCanvas) with HasBottomRight {

  type L <:
    VisualLink

  val link =
    new SimpleObjectProperty[L]

  link.onInvalidate {
    styleClass.setAll(
      "entity",
      "link"
    )

    Facet
      .as[HasStyleClasses](link())
      .map(_.styleClasses)
      .foreach(styleClass.addAll(_))

    val selected =
      Facet
        .as[HasSelected](link())
        .exists(_.selected)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      selected
    )
  }

  link() =
    initialLink.asInstanceOf[L]

  //TODO! Remove all println() calls!

  protected override def handlePrimaryPressed(mouseEvent: MouseEvent): Unit = {
    Facet
      .as[HasSelected](link())
      .map(_.selected)
      .foreach(selected => {
        if (mouseEvent.controlDown) {
          val linksToSelect =
            if (selected)
              graph.selectedLinks - link()
            else
              graph.selectedLinks + link()

          controller.setSelection(
            selectedVertexes =
              graph.selectedVertexes,

            selectedLinks =
              linksToSelect
          )
        } else if (!selected) {
          controller.setSelection(
            selectedVertexes =
              Set(),

            selectedLinks =
              Set(link())
          )
        }
      })
  }

  override protected def handlePrimaryDoublePressed(mouseEvent: MouseEvent): Unit = {
    controller.editLink(link())
  }

  override def bottomRight: Point2D
}
