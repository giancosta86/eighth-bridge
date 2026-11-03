package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.VisualLink
import info.gianlucacosta.helios.fx.dialogs.Alerts

trait EditLink extends GraphCanvasController {
  type L <:
    VisualLink

  override def editLink(link: VisualLink): Unit = {
    while (true) {
      try {
        doEditLink(link.asInstanceOf[L])
          .map(graph.updateArc)
          .foreach(graph_=)

        return
      } catch {
        case ex: IllegalArgumentException =>
          Alerts.showWarning(ex.getMessage, "Edit link")
      }
    }

    throw new AssertionError()
  }

  protected def doEditLink(link: L): Option[L]
}
