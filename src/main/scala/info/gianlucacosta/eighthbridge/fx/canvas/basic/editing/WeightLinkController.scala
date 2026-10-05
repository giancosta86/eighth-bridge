package info.gianlucacosta.eighthbridge.fx.canvas.basic.editing

import info.gianlucacosta.eighthbridge.fx.canvas.VisualGraph
import info.gianlucacosta.eighthbridge.fx.canvas.basic.{BasicLink, BasicVertex}
import info.gianlucacosta.eighthbridge.graphs.features.Weighted
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
  * Mixin controller providing editing support for weighted links
  *
  * @tparam V Vertex
  * @tparam L Link
  */
trait WeightLinkController[V <: BasicVertex, L <: BasicLink with Weighted, G <: VisualGraph[V, L]]
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
      link.weightCopy(newWeight)
    })
  }
}
