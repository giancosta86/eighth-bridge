package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualArc, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

/**
 * Controller mixin enabling selection.
 */
trait DefineSelection extends GraphCanvasController {
  override def canDrawSelectionRectangle: Boolean =
    true

  override def setSelection(selectionVertexes: Set[VisualVertex], selectionArcs: Set[VisualArc]): Option[AnyVisualGraph] =
    Some(
      graph.setSelection(selectionVertexes, selectionArcs)
    )


  override def setVertexSelectedState(vertex: VisualVertex, selected: Boolean): Option[AnyVisualGraph] =
    Some(
      graph.updateVertex(
        vertex.setSelected(selected)
      )
    )


  override def setArcSelectedState(arc: VisualArc, selected: Boolean): Option[AnyVisualGraph] =
    Some(
      graph.updateArc(
        arc.setSelected(selected)
      )
    )
}
