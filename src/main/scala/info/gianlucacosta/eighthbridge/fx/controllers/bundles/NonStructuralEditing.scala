package info.gianlucacosta.eighthbridge.fx.controllers.bundles

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.DragSelectedVertexes

trait NonStructuralEditing extends GraphCanvasController
  with DragSelectedVertexes
  with Selection
  with LinkLabel
  with LinkHandles

