package info.gianlucacosta.eighthbridge.fx.canvas.basic

import info.gianlucacosta.eighthbridge.fx.canvas.{GraphCanvasNode, VisualGraph, VisualLink, VisualVertex}

trait BasicGraphCanvasNode[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]]
  extends GraphCanvasNode[V, L, G] {

  override def controller: BasicController[V, L, G] =
    super.controller.asInstanceOf[BasicController[V, L, G]]
}
