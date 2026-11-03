package info.gianlucacosta.eighthbridge.fx.controller.composite

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controller.primitive.DragSelectedVertexes

trait NonStructuralEditing extends GraphCanvasController
  with DragSelectedVertexes
  with Selection
  with LinkLabel
  with LinkHandles

