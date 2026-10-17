package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.Arc
import info.gianlucacosta.eighthbridge.theory.features.{Named, Timestamped}
import scalafx.geometry.Point2D

/**
  * An arc for VisualGraph
  */
class VisualArc(arrowEndValue: Option[ArcArrowEnd])
  extends Arc with Timestamped with Cloneable {
  private var _timestamp: Long =
    System.nanoTime()

  private var _selected: Boolean =
    false

  private var _handles: List[ArcHandle] =
    List()

  private var _styleClasses: Set[String] =
    Set()

  private var _arrowEnd: Option[ArcArrowEnd] =
    arrowEndValue

  def timestamp: Long =
    _timestamp

  def setTimestamp(timestamp: Long): this.type = {
    val result: this.type =
      clone().asInstanceOf[this.type]

    result._timestamp =
      timestamp

    result
  }

  def selected: Boolean =
    _selected

  def setSelected(selected: Boolean): this.type =
    if (selected != _selected) {
      val result =
        updateTimestamp()

      result._selected = selected

      result
    } else
      this

  def handles: List[ArcHandle] =
    _handles

  def setHandles(handles: List[ArcHandle]): this.type =
    if (handles != _handles) {
      val result =
        updateTimestamp()

      result._handles =
        handles

      result
    } else
      this

  def styleClasses: Set[String] =
    _styleClasses

  def setStyleclasses(styleClasses: Set[String]): this.type =
    if (styleClasses != _styleClasses) {
      val result =
        updateTimestamp()

      result._styleClasses =
        styleClasses

      result
    } else
      this

  def arrowEnd: Option[ArcArrowEnd] =
    _arrowEnd

  def setArrowEnd(arrowEnd: Option[ArcArrowEnd]): this.type =
    if (arrowEnd != _arrowEnd) {
      val result =
        updateTimestamp()

      result._arrowEnd =
        arrowEnd

      result
    } else
      this
}