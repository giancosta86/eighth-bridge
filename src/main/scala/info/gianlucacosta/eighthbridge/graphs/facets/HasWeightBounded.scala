package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.helios.mathutils.Numbers

trait HasWeightBounded extends HasWeight {
  def minWeight: Weight

  def maxWeight: Weight

  override def toString: String =
    if (maxWeight < Weight.PositiveInfinity)
      s"(${Numbers.smartString(weight)}; ${Numbers.smartString(maxWeight)}"
    else
      super.toString
}
