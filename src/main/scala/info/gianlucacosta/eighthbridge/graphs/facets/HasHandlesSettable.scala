package info.gianlucacosta.eighthbridge.graphs.facets

trait HasHandlesSettable extends TimestampedPrototype with HasHandles {
  private var _handles: List[LinkHandle] =
    Nil

  override def handles: List[LinkHandle] =
    _handles

  def setHandles(handles: List[LinkHandle]): this.type = {
    if (handles.ne(_handles)) {
      val result =
        clone()

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
    val updatedHandles =
      handles.update(updatedHandle)

    setHandles(updatedHandles)
  }

  def removeHandle(handle: LinkHandle): this.type = {
    val updatedHandles =
      handles.filter(_ != handle)

    setHandles(
      updatedHandles
    )
  }
}
