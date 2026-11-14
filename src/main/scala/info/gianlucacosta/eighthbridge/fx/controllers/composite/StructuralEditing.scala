package info.gianlucacosta.eighthbridge.fx.controllers.composite

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.DeleteSelection

trait StructuralEditing extends GraphCanvasController
  with NonStructuralEditing
  with DeleteSelection