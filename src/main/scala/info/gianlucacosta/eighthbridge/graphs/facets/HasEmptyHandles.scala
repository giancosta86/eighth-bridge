package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.Link

trait HasEmptyHandles
  extends Link
    with HasHandles  {
  override def handles: List[LinkHandle] =
    Nil
}
