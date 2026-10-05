package info.gianlucacosta.eighthbridge.fx.canvas

import info.gianlucacosta.eighthbridge.graphs.point2point.visual.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.css.PseudoClasses

import scala.collection.JavaConversions._

/**
  * JavaFX node rendering a VisualLink
  */
trait LinkNode[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] extends GraphCanvasNode[V, L, G] {

  private var _link: L = _

  /**
    * The underlying link, updated as rendering is performed
    *
    * @return
    */
  def link: L =
    _link


  private[canvas] def link_=(newLink: L): Unit =
    _link = newLink


  override def render(): Unit = {
    styleClass.setAll("link")
    styleClass.addAll(link.styleClasses)


    this.pseudoClassStateChanged(
      PseudoClasses.Selected,
      link.selected
    )
  }
}
