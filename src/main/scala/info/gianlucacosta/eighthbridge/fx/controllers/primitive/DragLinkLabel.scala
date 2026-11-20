package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.Delta2D._
import info.gianlucacosta.eighthbridge.fx.{Delta2D, GraphCanvasController, VisualLink}
import info.gianlucacosta.eighthbridge.core.facets.HasOptionalCustomLabelCenterSettable
import info.gianlucacosta.helios.fx.Includes.extendPoint2D
import scalafx.geometry.Point2D

trait DragLinkLabel extends GraphCanvasController {
  override def dragLinkLabel(link: VisualLink, currentCenter: Point2D, delta: Delta2D): Unit = {
    val updatedCenter =
      (currentCenter + delta).nonNegative

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
