package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.graphs.Link

trait HasDefaultArcBarbs
  extends Link
    with HasOptionalArcBarbs {
  override def arcBarbs: Option[ArcBarbs] =
    Some(ArcBarbs.Default)
}
