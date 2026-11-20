package info.gianlucacosta.eighthbridge.core.facets

import info.gianlucacosta.eighthbridge.core.Link

trait HasHandles extends Link {
  def handles: List[LinkHandle]
}
