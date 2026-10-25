package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.eighthbridge.graphs.features.Weighted
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
  * Mixin controller providing editing support for weighted arcs
  */
trait ArcWeightEditing
  extends ArcEditing {

  /*def getMinWeight(arc: VisualArc with Weighted): Double =
    0

  def getMaxWeight(arc: VisualArc with Weighted): Double =
    Double.PositiveInfinity

  override protected def doEditArc(arc: VisualArc): Option[VisualArc] = {
    val weightedArc = arc.asInstanceOf[VisualArc with Weighted]

    InputDialogs.askForDouble(
      message = "Weight:",
      initialValue = weightedArc.weight,
      minValue = getMinWeight(weightedArc),
      maxValue = getMaxWeight(weightedArc),
      header = "Edit link"
    )
      .map(newWeight => {
        weightedArc.setWeight(newWeight).asInstanceOf[VisualArc]
      })
  }*/
}
