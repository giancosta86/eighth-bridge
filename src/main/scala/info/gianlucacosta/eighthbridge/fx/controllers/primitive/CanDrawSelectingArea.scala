package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

trait CanDrawSelectingArea extends GraphCanvasController {
  override def canDrawSelectingArea: Boolean =
    true
}
