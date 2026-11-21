package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.facets.HasHandlesSettable
import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, LinkHandle, VisualLink}

trait DeleteLinkHandle extends GraphCanvasController {
  override def deleteLinkHandle(link: VisualLink, handle: LinkHandle): Unit = {
    val updatedLink =
      link
        .asInstanceOf[VisualLink with HasHandlesSettable]
        .removeHandle(handle)

    graph =
      graph.updateLink(updatedLink)
  }
}
