package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.Delta2D._
import info.gianlucacosta.eighthbridge.fx.{Delta2D, GraphCanvasController, VisualLink}
import info.gianlucacosta.eighthbridge.graphs.facets.{HasHandlesSettable, LinkHandle}
import info.gianlucacosta.helios.fx.Includes._

trait DragLinkHandle extends GraphCanvasController {
  override def dragLinkHandle(link: VisualLink, linkHandle: LinkHandle, delta: Delta2D): Unit = {
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
