package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualLink}
import info.gianlucacosta.eighthbridge.graphs.facets.{HasHandlesSettable, LinkHandle}
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import info.gianlucacosta.helios.fx.Includes._

trait DragLinkHandle extends GraphCanvasController {
  override def dragLinkHandle(link: VisualLink, linkHandle: LinkHandle, delta: Delta): Unit = {
    val updatedHandle =
      linkHandle.setCenter(
        (linkHandle.center + delta).nonNegative
      )

    val updatedLink =
      link
        .asInstanceOf[VisualLink with HasHandlesSettable]
        .updateHandle(updatedHandle)

    graph =
      graph.updateLink(updatedLink)
  }
}
