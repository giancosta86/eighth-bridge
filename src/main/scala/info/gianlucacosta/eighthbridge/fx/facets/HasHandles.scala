package info.gianlucacosta.eighthbridge.fx.facets

import info.gianlucacosta.eighthbridge.core.Link
import info.gianlucacosta.eighthbridge.fx.LinkHandle

trait HasHandles extends Link {
  def handles: List[LinkHandle]
}
