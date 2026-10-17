package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.Vertex
import info.gianlucacosta.eighthbridge.theory.features.Timestamped
import scalafx.geometry.Point2D

/**
  * A vertex for VisualGraph
  */
class VisualVertex(centerValue: Point2D)
  extends Vertex with Timestamped with Cloneable {
  private var _timestamp: Long =
    System.nanoTime()

  private var _center: Point2D =
    centerValue

  private var _selected: Boolean =
    false

  private var _styleClasses: Set[String] =
    Set()

  def timestamp: Long =
    _timestamp

  def setTimestamp(timestamp: Long): this.type = {
    val result: this.type =
      clone().asInstanceOf[this.type]

    result._timestamp =
      timestamp

    result
  }

  def center: Point2D =
    _center

  def setCenter(center: Point2D): this.type =
    if (center != _center) {
      val result =
        updateTimestamp()

      result._center =
        center

      result
    } else
      this

  def selected: Boolean =
    _selected

  def setSelected(selected: Boolean): this.type =
    if (selected != _selected) {
      val result =
        updateTimestamp()

      result._selected =
        selected

      result
    } else
      this

  def styleClasses: Set[String] =
    _styleClasses

  def setStyleClasses(styleClasses: Set[String]): this.type =
    if (styleClasses != _styleClasses) {
      val result =
        updateTimestamp()

      result._styleClasses =
        styleClasses

      result
    } else
      this
}
