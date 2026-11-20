package info.gianlucacosta.eighthbridge.core.directed

trait HasDefaultBarbs
  extends Arc {
  override def barbs: ArcBarbs =
    ArcBarbs.Default
}
