package info.gianlucacosta.eighthbridge.graphs.features

import info.gianlucacosta.eighthbridge.fx.graph.ArcBarbs
import info.gianlucacosta.eighthbridge.graphs.Link

trait HasOptionalArcBarbs extends Link {
  def barbs: Option[ArcBarbs]
}
