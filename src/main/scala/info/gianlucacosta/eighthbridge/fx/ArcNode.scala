package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty

import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafx.scene.Group

/**
  * JavaFX node rendering a VisualArc
  */
abstract class ArcNode(graphCanvas: GraphCanvas, val arc: VisualArc) extends GraphCanvasNode(graphCanvas) {
  protected[fx] def updateFxProps(updatedArc: VisualArc): Unit = {
    styleClass.setAll("arc")
    styleClass.addAll(updatedArc.styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      updatedArc.selected
    )
  }
}
