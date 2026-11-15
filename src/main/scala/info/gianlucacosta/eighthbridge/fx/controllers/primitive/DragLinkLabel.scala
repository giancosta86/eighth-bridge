package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.Delta2D._
import info.gianlucacosta.eighthbridge.fx.{Delta2D, GraphCanvasController, VisualLink}
import info.gianlucacosta.eighthbridge.graphs.facets.HasOptionalCustomLabelCenterSettable
import scalafx.geometry.Point2D

trait DragLinkLabel extends GraphCanvasController {
  override def dragLinkLabel(link: VisualLink, actualCenter: Point2D, delta: Delta2D): Unit = {
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
