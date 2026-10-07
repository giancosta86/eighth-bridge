package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.dialogs.Alerts

trait VertexEditing[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]] extends GraphCanvasController[V, L, G] {
  override def editVertex(vertex: V): Option[G] = {
    while (true) {
      try {
        return doEditVertex(vertex).map(
          graph.replaceVertex(_)
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
  protected def doEditVertex(vertex: V): Option[V]
}
