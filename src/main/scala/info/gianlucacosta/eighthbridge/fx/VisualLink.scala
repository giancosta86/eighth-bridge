package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.graphs.Link
import scalafx.geometry.Point2D

/**
  * A link for VisualGraph
  */
trait VisualLink extends Link {
  def text: String

  def internalPoints: List[Point2D]

  def selected: Boolean

  def labelCenter: Option[Point2D]

  def arrow: LinkArrow =
    LinkArrow.Default

  def handleRadius: LinkHandleRadius =
    LinkHandleRadius.Default

  //TODO! Should I copy these, too?
  def styleClasses: List[String]

  def visualCopy(
                  text: String = text,
                  internalPoints: List[Point2D] = internalPoints,
                  selected: Boolean = selected,
                  labelCenter: Option[Point2D] = labelCenter,
                  arrow: LinkArrow = arrow,
                  handleRadius: LinkHandleRadius = handleRadius
                ): this.type

  override def toString: String =
    text
}