package info.gianlucacosta.eighthbridge.core.directed

import info.gianlucacosta.eighthbridge.core.facets.Barbs

trait HasDefaultBarbs
  extends Arc {
  override def barbs: Barbs =
    Barbs.Default
}
