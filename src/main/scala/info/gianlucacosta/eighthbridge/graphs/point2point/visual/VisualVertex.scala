package info.gianlucacosta.eighthbridge.graphs.point2point.visual

import info.gianlucacosta.eighthbridge.graphs.Vertex

import scalafx.geometry.Point2D

/**
  * A vertex for VisualGraph
  */
trait VisualVertex extends Vertex {
  def center: Point2D

  def selected: Boolean

  def styleClasses: List[String]

  def visualCopy(center: Point2D = center,
                 selected: Boolean = selected): this.type
}
