package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.Link

trait HasNoArcBarbs
  extends Link
    with HasOptionalArcBarbs {
  override def arcBarbs: Option[ArcBarbs] =
    None
}
