package info.gianlucacosta.eighthbridge.fx.controllers.composite

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.{CanDrawSelectingArea, SetSelection}

trait Selection extends GraphCanvasController
  with CanDrawSelectingArea
  with SetSelection