package info.gianlucacosta.eighthbridge.fx.controllers.bundles

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.DeleteSelection

trait StructuralEditing extends GraphCanvasController
  with NonStructuralEditing
  with DeleteSelection