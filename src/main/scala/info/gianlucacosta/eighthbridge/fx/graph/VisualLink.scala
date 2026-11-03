package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.features.{ConcreteCloneable, Named, Timestamp, Timestamped}

/**
  * A link for VisualGraph
  */
class VisualLink(barbsValue: Option[ArcBarbs])
  extends ConcreteCloneable with Link with Timestamped {

  private var _selected: Boolean =
    false

  private var _handles: List[LinkHandle] =
    List()

  private var _styleClasses: Set[String] =
    Set()

  private var _barbs: Option[ArcBarbs] =
    barbsValue

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

  def handles: List[LinkHandle] =
    _handles

  def setHandles(handles: List[LinkHandle]): this.type = {
    if (handles.ne(_handles)) {
      val result =
        updateTimestamp()

      result._handles =
        handles

      result
    } else
      this
  }

  def addHandle(index: Int, handle: LinkHandle): this.type = {
    val updatedHandles: List[LinkHandle] =
      handles.take(index) ++
        List(handle) ++
        handles.drop(index)

    setHandles(updatedHandles)
  }

  def updateHandle(updatedHandle: LinkHandle): this.type = {
    //println(s"UPDATING HANDLE: ${handle}")

    setHandles(
      handles.map(handle =>
        if (handle == updatedHandle)
          updatedHandle
        else
          handle
      )
    )
  }

  def removeHandle(handle: LinkHandle): this.type =
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