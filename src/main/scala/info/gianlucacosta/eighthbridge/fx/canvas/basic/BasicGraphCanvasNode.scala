package info.gianlucacosta.eighthbridge.fx.canvas.basic

import info.gianlucacosta.eighthbridge.fx.canvas.GraphCanvasNode
import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualGraph

trait BasicGraphCanvasNode[V <: BasicVertex[V], L <: BasicLink[L], G <: VisualGraph[V, L, G]]
  extends GraphCanvasNode[V, L, G] {

  override def controller: BasicController[V, L, G] =
    super.controller.asInstanceOf[BasicController[V, L, G]]
}
