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
  override protected def doEditLink(link: L): Option[L] = {
    val newWeightOption =
      InputDialogs.askForDouble(
        message = "Weight:",
        initialValue = link.weight,
        minValue = link.minWeight,
        maxValue = link.maxWeight,
        header = "Edit link"
      )

    newWeightOption.map(newWeight => {
      //TODO! What if checkWeight() crashes?
      //TODO! The same applies when setting a vertex name
      link.setWeight(newWeight)
    })
  }
}
