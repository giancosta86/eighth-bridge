package info.gianlucacosta.eighthbridge.fx.canvas

import info.gianlucacosta.eighthbridge.graphs.point2point.visual.{VisualGraph, VisualLink, VisualVertex}

/**
  * JavaFX node rendering the graph background and the selection rectangle
  */
trait BackgroundNode[
V <: VisualVertex[V],
L <: VisualLink[L],
G <: VisualGraph[V, L, G]
] extends GraphCanvasNode[V, L, G]