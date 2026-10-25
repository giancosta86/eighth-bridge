package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.helios.fx.dialogs.Alerts

trait ArcEditing extends GraphCanvasController {
/*  override def editArc(arc: VisualArc): Option[VisualGraph] = {
    while (true) {
      try {
        return doEditArc(arc).map(updatedArc =>
          graph.updateArc(updatedArc)
        )
      } catch {
        case ex: IllegalArgumentException =>
          Alerts.showWarning(ex.getMessage, "Edit link")
      }
    }

    throw new AssertionError()
  }


  /**
   * Interacts with the user about the arc properties.
   *
   * It can throw IllegalArgumentException, making the system notify the error and ask again.
   *
   * @param arc  The arc to edit
   * @return Some(new arc) if the editing is complete, None if the user canceled the editing
   *
   */
  protected def doEditArc(arc: VisualArc): Option[VisualArc]*/
}
