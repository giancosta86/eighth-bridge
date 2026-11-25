package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.facets.HasHandlesSettable
import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, LinkHandle, VisualLink}
import scalafx.geometry.Point2D

trait AddLinkHandle extends GraphCanvasController {
  override def addLinkHandle(link: VisualLink, index: Int, center: Point2D): Unit = {
    val linkHandle =
      createLinkHandle(center)

    val updatedLink =
      link
        .asInstanceOf[VisualLink with HasHandlesSettable]
        .addHandle(index, linkHandle)

    graph =
      graph.updateLink(updatedLink)
  }

  protected def createLinkHandle(center: Point2D): LinkHandle =
    LinkHandle.createDefault(center)
}
