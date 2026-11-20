package info.gianlucacosta.eighthbridge.core.facets

import info.gianlucacosta.helios.mathutils.Numbers

trait HasWeightBounded extends HasWeight {
  def demand: Weight

  def capacity: Weight

  override def toString: String =
    if (capacity < Weight.PositiveInfinity)
      s"(${Numbers.smartString(weight)}; ${Numbers.smartString(capacity)}"
    else
      super.toString
}
