package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

trait AdvancedLayoutEditing extends GraphCanvasController
  with LayoutEditing
  with DeleteSelection
