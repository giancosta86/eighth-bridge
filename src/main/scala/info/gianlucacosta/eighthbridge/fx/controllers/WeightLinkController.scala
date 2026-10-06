package info.gianlucacosta.eighthbridge.fx.controllers

import info.gianlucacosta.eighthbridge.fx.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.graphs.features.Weighted
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
  * Mixin controller providing editing support for weighted links
  *
  * @tparam V Vertex
  * @tparam L Link
  */
trait WeightLinkController[V <: VisualVertex, L <: VisualLink with Weighted, G <: VisualGraph[V, L]]
  extends InteractiveEditingController[V, L, G] {
  override protected def interactiveLinkEditing(graph: G, link: L): Option[L] = {
    val newWeightOption =
      InputDialogs.askForDouble(
        "Weight:",
        link.weight,
        link.minWeight,
        link.maxWeight,
        "Edit link"
      )

    newWeightOption.map(newWeight => {
      //TODO! What if checkWeight() crashes?
      //TODO! The same applies when setting a vertex name
      link.setWeight(newWeight)
    })
  }
}
