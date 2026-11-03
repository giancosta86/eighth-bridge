package info.gianlucacosta.eighthbridge.fx.controller.composite

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controller.primitive.{CanDrawSelectingArea, SetSelection}

trait Selection extends GraphCanvasController
  with CanDrawSelectingArea
  with SetSelection