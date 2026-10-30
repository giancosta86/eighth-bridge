package info.gianlucacosta.eighthbridge.fx.controller.composite

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controller.primitive.{AddLinkHandle, DeleteLinkHandle, DragLinkHandle}

trait LinkHandles extends GraphCanvasController
  with AddLinkHandle
  with DragLinkHandle
  with DeleteLinkHandle
