package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.theory.features.Weighted
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
  * Mixin controller providing editing support for weighted links
  *
  * @tparam V Vertex
  * @tparam L Link
  */
trait LinkWeightEditing
  extends LinkEditing {

  def getMinWeight(link: VisualLink with Weighted): Double =
    0

  def getMaxWeight(link: VisualLink with Weighted): Double =
    Double.PositiveInfinity

  override protected def doEditLink(link: VisualLink): Option[VisualLink] = {
    val weightedLink = link.asInstanceOf[VisualLink with Weighted]

    InputDialogs.askForDouble(
      message = "Weight:",
      initialValue = weightedLink.weight,
      minValue = getMinWeight(weightedLink),
      maxValue = getMaxWeight(weightedLink),
      header = "Edit link"
    )
      .map(newWeight => {
        weightedLink.setWeight(newWeight).asInstanceOf[VisualLink]
      })
  }
}
