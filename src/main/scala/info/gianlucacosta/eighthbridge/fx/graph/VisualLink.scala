package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.Link
import info.gianlucacosta.eighthbridge.theory.features.Named
import scalafx.geometry.Point2D

/**
  * A link for VisualGraph
  */
trait VisualLink extends Link with Named {
  def selected: Boolean

  def setSelected(value: Boolean): this.type

  def labelCenter: Option[Point2D]

  def setLabelCenter(value: Option[Point2D]): this.type

  def setName(value: String): this.type

  def internalPoints: List[Point2D]

  def setInternalPoints(value: List[Point2D]): this.type

  def styleClasses: Set[String]

  def setStyleclasses(value: Set[String]): this.type

  //TODO! These go here or in the DefaultVisualLink implementation? + use the .Default values
  def arrow: LinkArrow

  def setArrow(value: LinkArrow): this.type

  def handleRadius: LinkHandleRadius

  def setHandleRadius(value: LinkHandleRadius): this.type
}