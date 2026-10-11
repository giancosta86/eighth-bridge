package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses
import javafx.beans.property.SimpleObjectProperty

import scala.collection.JavaConversions._
import scalafx.Includes._
import scalafx.scene.Group

/**
  * JavaFX node rendering a VisualLink
  */
abstract class LinkNode extends Group with GraphCanvasNode {
  val link: VisualLink

  val currentLink: SimpleObjectProperty[VisualLink] =
    new SimpleObjectProperty(link)

  currentLink.addListener((_: javafx.beans.Observable) => {
    updateFx()
  })

  protected[fx] def updateFx(): Unit = {
    styleClass.setAll("link")
    styleClass.addAll(currentLink().styleClasses)

    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      currentLink().selected
    )
  }
}
