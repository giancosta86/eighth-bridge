package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

/**
 * Controller mixin enabling selection.
 */
trait DefineSelection extends GraphCanvasController {
  override def setSelection(selectedVertexes: Set[VisualVertex], selectedArcs: Set[VisualArc]): Option[VisualGraph] =
    Some(
      graph.setSelection(
        selectedVertexes,
        selectedArcs
      )
    )
}
