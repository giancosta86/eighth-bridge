package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualGraph, VisualLink, VisualVertex}

trait Undirected[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]] extends GraphCanvasController[V, L, G] {
  override def renderDirected: Boolean = false
}
