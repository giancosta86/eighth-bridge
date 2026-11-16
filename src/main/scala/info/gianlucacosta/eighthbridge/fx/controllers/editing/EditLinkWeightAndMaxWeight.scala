package info.gianlucacosta.eighthbridge.fx.controllers.editing

import info.gianlucacosta.eighthbridge.fx.VisualLink
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.EditLink
import info.gianlucacosta.eighthbridge.graphs.facets.{HasWeightBoundedSettable, Weight}
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

//TODO! Editing a link weight should also update its label?!
trait EditLinkWeightAndMaxWeight extends EditLink {
  override type L <:
    VisualLink with HasWeightBoundedSettable

  def minValueForMaxWeight: Weight =
    0

  def maxValueForMaxWeight: Weight =
    Double.MaxValue

  override def doEditLink(link: L): Option[L] = {
    InputDialogs.askForDouble(
        message = "Max weight:",
        initialValue = link.capacity,
        minValue = minValueForMaxWeight,
        maxValue = maxValueForMaxWeight,
        header = "Edit link"
      )
      .flatMap(newMaxWeight => {
        InputDialogs.askForDouble(
            message = "Weight:",
            initialValue = link.weight,
            minValue = link.demand,
            maxValue = newMaxWeight,
            header = "Edit link"
          )
          .map(newWeight =>
            link.setWeight(
              demand =
                link.demand,
              weight =
                newWeight,
              capacity =
                newMaxWeight
            )
          )
      })
  }
}
