package info.gianlucacosta.eighthbridge.core.facets

object Timestamp {
  def now: Timestamp =
    System.nanoTime()
}
