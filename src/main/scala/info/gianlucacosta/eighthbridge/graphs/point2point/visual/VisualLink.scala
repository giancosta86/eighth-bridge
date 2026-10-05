package info.gianlucacosta.eighthbridge.graphs.point2point.visual

import info.gianlucacosta.eighthbridge.graphs.Link

import scalafx.geometry.Point2D

/**
  * A link for VisualGraph
  */
trait VisualLink extends Link {
  def internalPoints: List[Point2D]

  def selected: Boolean

  def labelCenter: Option[Point2D]

  def styleClasses: List[String]

  def visualCopy(
                  internalPoints: List[Point2D] = internalPoints,
                  selected: Boolean = selected,
                  labelCenter: Option[Point2D] = labelCenter): this.type
}