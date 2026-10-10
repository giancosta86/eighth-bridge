package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.Vertex
import scalafx.geometry.Point2D

/**
  * A vertex for VisualGraph
  */
trait VisualVertex extends Vertex {
  def center: Point2D

  def setCenter(value: Point2D): this.type

  def selected: Boolean

  def setSelected(value: Boolean): this.type

  def styleClasses: Set[String]

  def setStyleClasses(value: Set[String]): this.type
}
