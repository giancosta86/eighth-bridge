package info.gianlucacosta.eighthbridge.fx.canvas

import info.gianlucacosta.eighthbridge.fx.canvas.controllers.BasicController

trait DefaultGraphCanvasNode[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]]
  extends GraphCanvasNode[V, L, G] {

  override def controller: BasicController[V, L, G] =
    super.controller.asInstanceOf[BasicController[V, L, G]]
}
