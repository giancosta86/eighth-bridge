package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, VisualLink}
import info.gianlucacosta.eighthbridge.graphs.facets.HasOptionalCustomLabelCenterSettable
import scalafx.geometry.Point2D

trait DragLinkLabel extends GraphCanvasController {
  override def dragLinkLabel(link: VisualLink, actualCenter: Point2D, delta: Delta): Unit = {
    val updatedCenter =
      actualCenter + delta

    val updatedLink =
      link
        .asInstanceOf[VisualLink with HasOptionalCustomLabelCenterSettable]
        .setCustomLabelCenter(
          Some(updatedCenter)
        )

    graph =
      graph.updateLink(updatedLink)
  }
}
