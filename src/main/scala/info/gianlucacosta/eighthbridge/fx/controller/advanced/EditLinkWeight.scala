package info.gianlucacosta.eighthbridge.fx.controller.advanced

import info.gianlucacosta.eighthbridge.fx.controller.primitive.EditLink
import info.gianlucacosta.eighthbridge.fx.graph.VisualLink
import info.gianlucacosta.eighthbridge.graphs.features.Weighted
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

trait EditLinkWeight
  extends EditLink {
  override type L <:
    VisualLink with Weighted

  override protected def doEditLink(link: L): Option[L] = {
    InputDialogs.askForDouble(
      message = "Weight:",
      initialValue = link.weight,
      minValue = link.minWeight,
      maxValue = link.maxWeight,
      header = "Edit link"
    )
      .map(newWeight =>
        link.setWeight(newWeight)
      )
  }
}
