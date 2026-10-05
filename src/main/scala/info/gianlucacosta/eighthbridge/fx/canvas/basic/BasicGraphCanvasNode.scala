package info.gianlucacosta.eighthbridge.fx.canvas.basic

import info.gianlucacosta.eighthbridge.fx.canvas.{GraphCanvasNode, VisualGraph}

trait BasicGraphCanvasNode[V <: BasicVertex, L <: BasicLink, G <: VisualGraph[V, L]]
  extends GraphCanvasNode[V, L, G] {

  override def controller: BasicController[V, L, G] =
    super.controller.asInstanceOf[BasicController[V, L, G]]
}
