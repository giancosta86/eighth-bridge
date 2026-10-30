package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

trait DeleteSelection extends GraphCanvasController {
  override def deleteSelection(): Unit = {
    graph =
      graph
        .removeArcs(graph.selectedArcs)
        .removeVertexes(graph.selectedVertexes)
  }
}
