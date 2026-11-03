package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.LabeledVisualLink

trait ResetLinkLabelCenter extends GraphCanvasController {
  override def resetLinkLabelCenter(link: LabeledVisualLink): Unit = {
    val updatedLink =
      link
        .setCustomLabelCenter(None)

    graph =
      graph.updateArc(updatedLink)
  }
}
