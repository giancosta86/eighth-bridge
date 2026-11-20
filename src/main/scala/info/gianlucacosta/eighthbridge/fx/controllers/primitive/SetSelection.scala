package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualLink, VisualVertex}

/**
 * Controller mixin enabling selection.
 */
trait SetSelection extends GraphCanvasController {
  override def setSelection(selectedVertexes: Set[VisualVertex], selectedLinks: Set[VisualLink]): Unit = {
    graph =
      graph.setSelection(
        selectedVertexes,
        selectedLinks
      )
  }
}
