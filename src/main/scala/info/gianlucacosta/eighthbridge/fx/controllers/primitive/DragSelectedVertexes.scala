package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.{Delta2D, GraphCanvasController}

trait DragSelectedVertexes extends GraphCanvasController {
  override def dragSelectedVertexes(delta: Delta2D): Unit = {
    graph =
      graph.moveSelectedVertexesBy(delta)
  }
}
