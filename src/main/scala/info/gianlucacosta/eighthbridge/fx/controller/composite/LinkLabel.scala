package info.gianlucacosta.eighthbridge.fx.controller.composite

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controller.primitive.{DragLinkLabel, ResetLinkLabelCenter}

trait LinkLabel extends GraphCanvasController
  with DragLinkLabel
  with ResetLinkLabelCenter
