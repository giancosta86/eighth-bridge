package info.gianlucacosta.eighthbridge.fx.controller.composite

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controller.primitive.DeleteSelection

trait StructuralEditing extends GraphCanvasController
  with NonStructuralEditing
  with DeleteSelection