package info.gianlucacosta.eighthbridge.graphs.features

trait NonNegativeWeighted extends Weighted {
  override val minWeight: Double = 0
  override val maxWeight: Double = Double.PositiveInfinity
}
