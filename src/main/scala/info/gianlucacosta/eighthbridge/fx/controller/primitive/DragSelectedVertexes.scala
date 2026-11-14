package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.Delta

trait DragSelectedVertexes extends GraphCanvasController {
  override def dragSelectedVertexes(delta: Delta): Unit = {
    graph =
      graph.moveSelectedVertexesBy(delta)
  }
}
