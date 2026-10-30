package info.gianlucacosta.eighthbridge.fx.controller.advanced

import info.gianlucacosta.eighthbridge.fx.controller.primitive.EditLink
import info.gianlucacosta.eighthbridge.fx.graph.VisualLink
import info.gianlucacosta.eighthbridge.graphs.features.{Weight, Weighted}
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
  * Mixin controller providing editing support for weighted arcs
  */
trait EditLinkWeight
  extends EditLink {
  override type L <:
    VisualLink with Weighted

  def getMinWeight(link: L): Weight =
    0

  def getMaxWeight(link: L): Weight =
    Weight.PositiveInfinity

  override protected def doEditLink(link: L): Option[L] = {
    InputDialogs.askForDouble(
      message = "Weight:",
      initialValue = link.weight,
      minValue = getMinWeight(link),
      maxValue = getMaxWeight(link),
      header = "Edit link"
    )
      .map(newWeight => {
        link.setWeight(newWeight)
      })
  }
}
