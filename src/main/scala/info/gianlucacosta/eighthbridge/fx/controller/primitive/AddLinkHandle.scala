package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.VisualLink
import info.gianlucacosta.eighthbridge.graphs.facets.{HasHandles, HasHandlesSettable, LinkHandle}
import scalafx.geometry.Point2D

trait AddLinkHandle extends GraphCanvasController {
  override def addLinkHandle(link: VisualLink, index: Int, point: Point2D): Unit = {
    val linkHandle =
      LinkHandle.createDefault(point)

    val updatedLink =
      link
        .asInstanceOf[VisualLink with HasHandlesSettable]
        .addHandle(index, linkHandle)

    graph =
      graph.updateLink(updatedLink)
  }
}
