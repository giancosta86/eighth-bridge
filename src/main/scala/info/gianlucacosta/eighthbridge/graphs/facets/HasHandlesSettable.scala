package info.gianlucacosta.eighthbridge.graphs.facets

trait HasHandlesSettable extends HasTimestampSettable with HasHandles {
  protected val initialHandles: List[LinkHandle] =
    List()

  private var _handles: List[LinkHandle] =
    initialHandles

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

  def updateHandle(updatedHandle: LinkHandle): this.type =
    setHandles(
      handles.map(handle =>
        if (handle == updatedHandle)
          updatedHandle
        else
          handle
      )
    )

  def removeHandle(handle: LinkHandle): this.type =
    setHandles(
      handles.filter(_ != handle)
    )
}
