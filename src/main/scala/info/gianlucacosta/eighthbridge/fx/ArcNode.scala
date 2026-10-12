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
abstract class ArcNode(initialArc: VisualArc) extends Group with GraphCanvasNode {
  val arc: VisualArc

  val currentArc: SimpleObjectProperty[VisualArc] =
    new SimpleObjectProperty(arc)

  currentArc.addListener((_: javafx.beans.Observable) => {
    updateFx()
  })

  protected[fx] def updateFx(): Unit = {
    styleClass.setAll("arc")
    styleClass.addAll(currentArc().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      currentArc().selected
    )
  }
}
