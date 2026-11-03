package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, LinkHandle, VisualLink}

trait DragLinkHandle extends GraphCanvasController {
  override def dragLinkHandle(link: VisualLink, linkHandle: LinkHandle, delta: Delta): Unit = {
    val updatedLink =
      link.updateHandle(
        linkHandle.moveBy(delta)
      )

    graph =
      graph.updateArc(updatedLink)
  }
}
