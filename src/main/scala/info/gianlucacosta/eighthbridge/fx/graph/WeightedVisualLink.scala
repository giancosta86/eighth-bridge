package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.features.{Weight, Weighted}
import info.gianlucacosta.helios.mathutils.Numbers

object WeightedVisualLink {
  private def formatLabel(weight: Weight, maxWeight: Weight): String =
    if (maxWeight < Weight.PositiveInfinity)
      s"(${Numbers.smartString(weight)}; ${Numbers.smartString(maxWeight)}"
    else
      Numbers.smartString(weight)

  private def checkConstraints(
                              minWeight: Weight,
                              weight: Weight,
                              maxWeight: Weight
                              ): Unit = {
    require(
      minWeight <= weight,
      s"It must be minWeight(${Numbers.smartString(minWeight)}) <= weight(${Numbers.smartString(weight)})"
    )

    require(
      weight <= maxWeight,
      s"It must be weight(${Numbers.smartString(weight)}) <= maxWeight(${Numbers.smartString(maxWeight)})"
    )
  }
}

class WeightedVisualLink(
                          barbsValue: Option[ArcBarbs],
                          minWeightValue: Weight = 0,
                          weightValue: Weight = 0,
                          maxWeightValue: Weight = 0
                        )
  extends LabeledVisualLink(
    barbsValue,
    labelValue =
      WeightedVisualLink.formatLabel(
        weight =
          weightValue,
        maxWeight =
          maxWeightValue
    )
) with Weighted {
  WeightedVisualLink.checkConstraints(
    minWeight =
      minWeightValue,
    weight =
      weightValue,
    maxWeight =
      maxWeightValue
  )

  private var _minWeight: Weight =
    minWeightValue

  private var _weight: Weight =
    weightValue

  private var _maxWeight: Weight =
    maxWeightValue

  override def minWeight: Weight =
    _minWeight

  override def weight: Weight =
    _weight

  override def maxWeight: Weight =
    _maxWeight

  override def setWeight(minWeight: Weight, weight: Weight, maxWeight: Weight): this.type = {
    if (minWeight != _minWeight ||
      weight != _weight ||
      maxWeight != _maxWeight
    ) {
      WeightedVisualLink.checkConstraints(
        minWeight =
          minWeight,
        weight =
          weight,
        maxWeight =
          maxWeight
      )

      val result =
        updateTimestamp()
          .setLabel(
            WeightedVisualLink.formatLabel(
              weight =
                weight,
              maxWeight =
                maxWeight
            )
          )

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
