package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.fx.graph.ArcBarbs
import info.gianlucacosta.eighthbridge.graphs.Link

trait HasOptionalArcBarbs extends Link {
  def arcBarbs: Option[ArcBarbs]
}
