package info.gianlucacosta.eighthbridge.fx.controller.primitive

import info.gianlucacosta.eighthbridge.fx.GraphCanvasController
import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.helios.fx.dialogs.Alerts

trait EditVertex extends GraphCanvasController {
  type V <:
    VisualVertex

  override def editVertex(vertex: VisualVertex): Unit = {
    while (true) {
      try {
        doEditVertex(vertex.asInstanceOf[V])
          .map(graph.updateVertex)
          .foreach(graph_=)

        return
      } catch {
        case ex: IllegalArgumentException =>
          Alerts.showWarning(ex.getMessage, "Edit vertex")
      }
    }

    throw new AssertionError()
  }

  protected def doEditVertex(vertex: V): Option[V]
}
