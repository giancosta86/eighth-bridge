package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

trait Undirected extends GraphCanvasController {
  override def renderDirected: Boolean = false
}
