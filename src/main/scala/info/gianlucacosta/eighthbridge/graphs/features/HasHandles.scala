package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.fx.graph.LinkHandle
import info.gianlucacosta.eighthbridge.graphs.Link

trait HasHandles extends Link {
  def handles: List[LinkHandle]
}
