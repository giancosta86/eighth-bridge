package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.{LinkHandle, VisualLink}
import scalafx.geometry.Point2D

trait AddLinkHandle extends GraphCanvasController {
  override def addLinkHandle(link: VisualLink, index: Int, point: Point2D): Unit = {
    val linkHandle =
      LinkHandle.makeDefaultCircle(point)

    val updatedLink =
      link.addHandle(index, linkHandle)

    graph =
      graph.updateArc(updatedLink)
  }
}
