package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualGraph, VisualLink, VisualVertex}

trait CoreEditing[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]] extends GraphCanvasController[V, L, G]
  with DefineSelection[V, L, G]
  with DeleteSelection[V, L, G]
  with DragDrop[V, L, G]
  with InternalLinkPoints[V, L, G]