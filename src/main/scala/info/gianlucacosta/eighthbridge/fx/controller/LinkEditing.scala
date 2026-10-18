package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.dialogs.Alerts

trait LinkEditing[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]] extends GraphCanvasController[V, L, G] {
  override def editLink(link: L): Option[G] = {
    while (true) {
      try {
        return doEditLink(link).map(
          graph.replaceLink(_)
        )
      } catch {
        case ex: IllegalArgumentException =>
          Alerts.showWarning(ex.getMessage, "Edit link")
      }
    }

    throw new AssertionError()
  }


  /**
   * Interacts with the user about the link properties.
   *
   * It can throw IllegalArgumentException, making the system notify the error and ask again.
   *
   * @param link  The link to edit
   * @return Some(new link) if the editing is complete, None if the user canceled the editing
   *
   */
  protected def doEditLink(link: L): Option[L]
}
