package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.{LinkHandle, VisualLink}

trait DeleteLinkHandle extends GraphCanvasController {
  override def deleteLinkHandle(link: VisualLink, handle: LinkHandle): Unit = {
    val updatedLink =
      link.removeHandle(handle)

    graph =
      graph.updateArc(updatedLink)
  }
}
