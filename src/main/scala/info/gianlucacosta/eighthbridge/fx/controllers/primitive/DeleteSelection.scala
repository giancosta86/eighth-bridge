package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

trait DeleteSelection extends GraphCanvasController {
  override def deleteSelection(): Unit = {
    graph =
      graph
        .removeLinks(graph.selectedLinks)
        .removeVertexes(graph.selectedVertexes)
  }
}
