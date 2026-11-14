package info.gianlucacosta.eighthbridge.graphs.directed

trait HasDefaultBarbs
  extends Arc {
  override def barbs: ArcBarbs =
    ArcBarbs.Default
}
