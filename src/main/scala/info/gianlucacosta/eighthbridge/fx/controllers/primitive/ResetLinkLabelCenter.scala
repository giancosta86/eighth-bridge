package info.gianlucacosta.eighthbridge.fx.controllers.primitive

import info.gianlucacosta.eighthbridge.fx.facets.HasOptionalCustomLabelCenterSettable
import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualLink}

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
