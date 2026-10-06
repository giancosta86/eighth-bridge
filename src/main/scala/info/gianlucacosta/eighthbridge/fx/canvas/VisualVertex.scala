package info.gianlucacosta.eighthbridge.fx.canvas

import info.gianlucacosta.eighthbridge.graphs.Vertex
import scalafx.geometry.Point2D

/**
  * A vertex for VisualGraph
  */
trait VisualVertex extends Vertex {
  def text: String

  def center: Point2D

  def selected: Boolean

  //TODO! Should I copy these, too?
  def styleClasses: List[String]

  def visualCopy(
                  text: String = text,
                  center: Point2D = center,
                 selected: Boolean = selected): this.type

  override def toString: String =
    text
}
