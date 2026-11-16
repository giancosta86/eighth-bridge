package info.gianlucacosta.eighthbridge.graphs.facets

import info.gianlucacosta.helios.mathutils.Numbers

trait HasWeightBoundedSettable extends TimestampedPrototype with HasWeightBounded {
  private var _demand: Weight =
    0

  override def demand: Weight =
    _demand

  private var _weight: Weight =
    _demand

  override def weight: Weight =
    _weight

  private var _capacity: Weight =
    Weight.PositiveInfinity

  override def capacity: Weight =
    _capacity

  def setWeight(
                 demand: Weight = _demand,
                 weight: Weight = _weight,
                 capacity: Weight = _capacity
 ): this.type =
    if (demand != _demand ||
      weight != _weight ||
      capacity != _capacity
    ) {
      require(
        demand <= weight,
        s"It must be demand(${Numbers.smartString(demand)}) <= weight(${Numbers.smartString(weight)})"
      )

      require(
        weight <= capacity,
        s"It must be weight(${Numbers.smartString(weight)}) <= capacity(${Numbers.smartString(capacity)})"
      )

      val result =
        clone()

      result._demand =
        demand

      result._weight =
        weight

      result._capacity =
        capacity

      result
    } else
      this
}
