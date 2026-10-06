package info.gianlucacosta.eighthbridge.graphs.features

trait NonNegativeWeighted extends Weighted {
  override def minWeight: Double = 0
  override def maxWeight: Double = Double.PositiveInfinity
}
