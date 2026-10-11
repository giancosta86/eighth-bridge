package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

/**
 * Controller mixin enabling selection.
 */
trait DefineSelection extends GraphCanvasController {
  override def canDrawSelectionRectangle: Boolean =
    true

  override def setSelection(selectionVertexes: Set[V], selectionLinks: Set[L]): Option[G] =
    Some(
      graph.setSelection(selectionVertexes, selectionLinks)
    )


  override def setVertexSelectedState(vertex: V, selected: Boolean): Option[G] =
    Some(
      graph.replaceVertex(
        vertex,
        vertex.visualCopy(selected = selected)
      )
    )


  override def setLinkSelectedState(link: L, selected: Boolean): Option[G] =
    Some(
      graph.replaceLink(
        link,
        link.visualCopy(selected = selected)
      )
    )
}
