package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty

import scala.collection.JavaConversions._
import scalafx.Includes._

/**
  * JavaFX node rendering a VisualLink
  */
trait LinkNode[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] extends GraphCanvasNode[V, L, G] {
  val linkInGraph: L

  val link: SimpleObjectProperty[L] =
    new SimpleObjectProperty(linkInGraph)

  link.addListener((_: javafx.beans.Observable) => {
    updateFx()
  })

  protected[fx] def updateFx(): Unit = {
    styleClass.setAll("link")
    styleClass.addAll(link().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      link().selected
    )
  }
}
