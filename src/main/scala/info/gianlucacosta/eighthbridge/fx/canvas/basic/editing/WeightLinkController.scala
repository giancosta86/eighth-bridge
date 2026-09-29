package info.gianlucacosta.eighthbridge.fx.canvas.basic.editing

import info.gianlucacosta.eighthbridge.fx.canvas.basic.{BasicLink, BasicVertex}
import info.gianlucacosta.eighthbridge.graphs.point2point.specific.Weighted
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualGraph
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
  * Mixin controller providing editing support for weighted links
  *
  * @tparam V Vertex
  * @tparam L Link
  */
trait WeightLinkController[V <: BasicVertex[V], L <: BasicLink[L] with Weighted[L], G <: VisualGraph[V, L, G]]
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
