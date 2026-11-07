package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.Delta._
import info.gianlucacosta.eighthbridge.fx.graph.{Delta, LabeledVisualLink, VisualLink}
import info.gianlucacosta.eighthbridge.graphs.facets.HasOptionalCustomLabelCenterSettable
import scalafx.geometry.Point2D

trait DragLinkLabel extends GraphCanvasController {
  override def dragLinkLabel(link: VisualLink with HasOptionalCustomLabelCenterSettable, actualCenter: Point2D, delta: Delta): Unit = {
    val updatedCenter =
      actualCenter + delta

    val updatedLink =
      link
        .setCustomLabelCenter(
          Some(updatedCenter)
        )

    graph =
      graph.updateArc(updatedLink)
  }
}
