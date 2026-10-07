package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualGraph, VisualLink, VisualVertex}

/**
 * Controller mixin enabling selection.
 */
trait DefineSelection[
  V <: VisualVertex,
  L <: VisualLink,
  G <: VisualGraph[V, L]
] extends GraphCanvasController[V, L, G] {
  override def canDrawSelectionRectangle: Boolean =
    true

  override def setSelection(selectionVertexes: Set[V], selectionLinks: Set[L]): Option[G] =
    Some(
      graph.setSelection(selectionVertexes, selectionLinks)
    )


  override def setVertexSelectedState(vertex: V, selected: Boolean): Option[G] =
    Some(
      graph.replaceVertex(
        vertex.visualCopy(selected = selected)
      )
    )


  override def setLinkSelectedState(link: L, selected: Boolean): Option[G] =
    Some(
      graph.replaceLink(
        link.visualCopy(selected = selected)
      )
    )
}
