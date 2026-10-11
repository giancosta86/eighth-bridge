package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.helios.fx.dialogs.Alerts

trait LinkEditing extends GraphCanvasController {
  override def editLink(link: VisualLink): Option[AnyVisualGraph] = {
    while (true) {
      try {
        return doEditLink(link).map(updatedLink =>
          graph.updateLink(updatedLink)
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
  protected def doEditLink(link: VisualLink): Option[VisualLink]
}
