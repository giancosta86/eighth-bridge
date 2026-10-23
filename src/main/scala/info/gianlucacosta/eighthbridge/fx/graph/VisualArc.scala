package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.theory.Arc
import info.gianlucacosta.eighthbridge.theory.features.{Named, Timestamped}
import scalafx.geometry.Point2D

/**
  * An arc for VisualGraph
  */
class VisualArc(barbsValue: Option[ArcBarbs])
  extends Arc with Timestamped with Cloneable {
  private var _timestamp: Long =
    System.nanoTime()

  private var _selected: Boolean =
    false

  private var _handles: List[ArcHandle] =
    List()

  private var _styleClasses: Set[String] =
    Set()

  private var _barbs: Option[ArcBarbs] =
    barbsValue

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

  private def setHandles(handles: List[ArcHandle]): this.type =
    if (handles != _handles) {
      val result =
        updateTimestamp()

      result._handles =
        handles

      result
    } else
      this

  def addHandle(index: Int, arcHandle: ArcHandle): this.type = {
    val updatedHandles: List[ArcHandle] =
      handles.take(index) ++
        List(arcHandle) ++
        handles.drop(index)

    setHandles(updatedHandles)
  }

  def replaceHandle(handle: ArcHandle): this.type =
    setHandles(
      handles.map(currentHandle =>
        if (currentHandle == handle)
          handle
        else
          currentHandle
      )
    )

  def removeHandle(handle: ArcHandle): this.type =
    setHandles(
      handles.filter(_ != handle)
    )

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

  def barbs: Option[ArcBarbs] =
    _barbs

  def setBarbs(barbs: Option[ArcBarbs]): this.type =
    if (barbs != _barbs) {
      val result =
        updateTimestamp()

      result._barbs =
        barbs

      result
    } else
      this
}