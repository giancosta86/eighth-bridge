package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.eighthbridge.fx.graph.WeightedVisualLink
import info.gianlucacosta.helios.mathutils.Numbers

trait HasWeightSettable extends HasTimestampSettable with HasWeight {
  protected val initialMinWeight: Weight =
    0

  private var _minWeight: Weight =
    initialMinWeight

  protected val initialWeight: Weight =
    initialMinWeight

  private var _weight: Weight =
    initialWeight

  protected val initialMaxWeight: Weight =
    Weight.PositiveInfinity

  private var _maxWeight: Weight =
    initialMaxWeight

  def minWeight: Weight =
    _minWeight

  override def weight: Weight =
    _weight

  def maxWeight: Weight =
    _maxWeight

  def setWeight(weight: Weight): this.type =
    setWeight(
      minWeight, weight, maxWeight
    )

  def setWeight(minWeight: Weight, weight: Weight, maxWeight: Weight): this.type = {
    if (minWeight != _minWeight ||
      weight != _weight ||
      maxWeight != _maxWeight
    ) {
      require(
        minWeight <= weight,
        s"It must be minWeight(${Numbers.smartString(minWeight)}) <= weight(${Numbers.smartString(weight)})"
      )

      require(
        weight <= maxWeight,
        s"It must be weight(${Numbers.smartString(weight)}) <= maxWeight(${Numbers.smartString(maxWeight)})"
      )

      val result =
        updateTimestamp()

      result._minWeight =
        minWeight

      result._weight =
        weight

      result._maxWeight =
        maxWeight

      result
    } else
      this
  }
}
