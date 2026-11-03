package info.gianlucacosta.eighthbridge.fx.controller.advanced

import info.gianlucacosta.eighthbridge.fx.controller.primitive.EditLink
import info.gianlucacosta.eighthbridge.fx.graph.VisualLink
import info.gianlucacosta.eighthbridge.graphs.features.{Weight, Weighted}
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

trait EditLinkWeightAndMaxWeight extends EditLink {
  override type L <:
    VisualLink with Weighted

  def minValueForMaxWeight: Weight =
    0

  def maxValueForMaxWeight: Weight =
    Double.MaxValue

  override def doEditLink(link: L): Option[L] = {
    InputDialogs.askForDouble(
        message = "Max weight:",
        initialValue = link.maxWeight,
        minValue = minValueForMaxWeight,
        maxValue = maxValueForMaxWeight,
        header = "Edit link"
      )
      .flatMap(newMaxWeight => {
        InputDialogs.askForDouble(
            message = "Weight:",
            initialValue = link.weight,
            minValue = link.minWeight,
            maxValue = newMaxWeight,
            header = "Edit link"
          )
          .map(newWeight =>
            link.setWeight(
              minWeight =
                link.minWeight,
              weight =
                newWeight,
              maxWeight =
                newMaxWeight
            )
          )
      })
  }
}
