package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.{Delta, GraphCanvasController}

trait DragSelectedVertexes extends GraphCanvasController {
  override def dragSelectedVertexes(delta: Delta): Unit = {
    graph =
      graph.moveSelectedVertexesBy(delta)
  }
}
