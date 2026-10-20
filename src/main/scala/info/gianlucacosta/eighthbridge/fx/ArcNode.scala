package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualArc, VisualGraph, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty

import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafx.scene.Group
import scalafx.scene.input.MouseEvent

/**
  * JavaFX node rendering a VisualArc
  */
abstract class ArcNode(graphCanvas: GraphCanvas, initialArc: VisualArc)
  extends GraphCanvasNode(graphCanvas) {
  private var _arc: VisualArc =
    initialArc

  def arc: VisualArc =
    _arc

  protected[fx] def update(updatedArc: VisualArc): Unit = {
    //TODO! Remove all println() calls!

    _arc =
      updatedArc

    styleClass.setAll("arc")
    styleClass.addAll(updatedArc.styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      updatedArc.selected
    )
  }
}
