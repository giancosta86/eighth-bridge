package info.gianlucacosta.eighthbridge.fx.controllers.bundles

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.{DragLinkLabel, ResetLinkLabelCenter}

trait LinkLabel extends GraphCanvasController
  with DragLinkLabel
  with ResetLinkLabelCenter
