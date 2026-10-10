package info.gianlucacosta.eighthbridge.fx.graph

import scalafx.geometry.Point2D

/**
  * A vertex for VisualGraph
  */
trait VisualVertex {
  //TODO! Del this later!
  def text: String

  def center: Point2D

  def selected: Boolean

  def styleClasses: Set[String]

  def visualCopy(
                  text: String = text,
                  center: Point2D = center,
                  selected: Boolean = selected,
                  styleClasses: Set[String] = styleClasses): this.type

  override def toString: String =
    text
}
