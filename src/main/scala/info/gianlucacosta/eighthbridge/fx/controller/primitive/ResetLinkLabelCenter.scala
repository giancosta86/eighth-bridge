package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.VisualLink
import info.gianlucacosta.eighthbridge.graphs.facets.HasOptionalCustomLabelCenterSettable

trait ResetLinkLabelCenter extends GraphCanvasController {
  override def resetLinkLabelCenter(link: VisualLink): Unit = {
    val updatedLink =
      link
        .asInstanceOf[VisualLink with HasOptionalCustomLabelCenterSettable]
        .setCustomLabelCenter(None)

    graph =
      graph.updateLink(updatedLink)
  }
}
