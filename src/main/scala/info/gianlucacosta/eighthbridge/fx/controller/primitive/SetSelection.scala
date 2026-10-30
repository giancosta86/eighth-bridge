package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.{VisualLink, VisualVertex}

/**
 * Controller mixin enabling selection.
 */
trait SetSelection extends GraphCanvasController {
  override def setSelection(selectedVertexes: Set[VisualVertex], selectedArcs: Set[VisualLink]): Unit = {
    graph =
      graph.setSelection(
        selectedVertexes,
        selectedArcs
      )
  }
}
