package info.gianlucacosta.eighthbridge.fx.controllers.bundles

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.{AddLinkHandle, DeleteLinkHandle, DragLinkHandle}

trait LinkHandles extends GraphCanvasController
  with AddLinkHandle
  with DragLinkHandle
  with DeleteLinkHandle
