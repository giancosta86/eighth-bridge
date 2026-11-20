package info.gianlucacosta.eighthbridge.fx.controllers.bundles

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.{CanDrawSelectingArea, SetSelection}

trait Selection extends GraphCanvasController
  with CanDrawSelectingArea
  with SetSelection