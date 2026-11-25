package info.gianlucacosta.eighthbridge.fx.controllers.editing

import info.gianlucacosta.eighthbridge.core.facets.{HasWeightBoundedSettable, Weight}
import info.gianlucacosta.eighthbridge.fx.VisualLink
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.EditLink
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

trait EditLinkWeightAndCapacity extends EditLink {
  override type L <:
    VisualLink with HasWeightBoundedSettable

  def minValueForCapacity: Weight =
    0

  def maxValueForCapacity: Weight =
    Weight.MaxValue

  override def doEditLink(link: L): Option[L] = {
    InputDialogs.askForDouble(
        message = "Capacity:",
        initialValue = link.capacity,
        minValue = minValueForCapacity,
        maxValue = maxValueForCapacity,
        header = "Edit link..."
      )
      .flatMap(newCapacity => {
        InputDialogs.askForDouble(
            message = "Weight:",
            initialValue = link.weight,
            minValue = link.demand,
            maxValue = newCapacity,
            header = "Edit link..."
          )
          .map(newWeight =>
            link.setWeight(
              weight =
                newWeight,
              capacity =
                newCapacity
            )
          )
      })
  }
}
