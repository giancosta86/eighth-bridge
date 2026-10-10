package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}

/**
  * JavaFX node rendering the graph background and the selection rectangle
  */
trait BackgroundNode[
V <: VisualVertex,
L <: VisualLink,
G <: VisualGraph[V, L]
] extends GraphCanvasNode[V, L, G]