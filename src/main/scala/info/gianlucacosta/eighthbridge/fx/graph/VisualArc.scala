package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.Arc
import info.gianlucacosta.eighthbridge.theory.features.{Named, Timestamped}
import scalafx.geometry.Point2D

/**
  * An arc for VisualGraph
  */
trait VisualArc
  extends Arc with Named with Timestamped {
  def selected: Boolean

  def setSelected(selected: Boolean): this.type

  def customLabelCenter: Option[Point2D]

  def setCustomLabelCenter(customLabelCenter: Option[Point2D]): this.type

  def handlePoints: List[ArcHandlePoint]

  def setHandlePoints(handlePoints: List[ArcHandlePoint]): this.type

  def styleClasses: Set[String]

  def setStyleclasses(styleClasses: Set[String]): this.type

  def arrowEnd: Option[ArcArrowEnd]

  def setArrowEnd(arrowEnd: Option[ArcArrowEnd]): this.type
}