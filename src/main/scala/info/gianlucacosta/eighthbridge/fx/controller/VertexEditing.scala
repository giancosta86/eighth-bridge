package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.helios.fx.dialogs.Alerts

trait VertexEditing extends GraphCanvasController {
  override def editVertex(vertex: VisualVertex): Option[AnyVisualGraph] = {
    while (true) {
      try {
        return doEditVertex(vertex).map(updatedVertex =>
          graph.updateVertex(updatedVertex)
        )
      } catch {
        case ex: IllegalArgumentException =>
          Alerts.showWarning(ex.getMessage, "Edit vertex")
      }
    }

    throw new AssertionError()
  }


  /**
   * Interacts with the user about the vertex properties.
   *
   * It can throw IllegalArgumentException, making the system notify the error and ask again.
   *
   * @param vertex The vertex to edit
   * @return Some(new vertex) if the editing is complete, None if the user canceled the editing
   *
   */
  protected def doEditVertex(vertex: VisualVertex): Option[VisualVertex]
}
