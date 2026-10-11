package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.graph.{AnyVisualGraph, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.fx.GraphCanvasController

/**
 * Controller mixin enabling selection.
 */
trait DefineSelection extends GraphCanvasController {
  override def canDrawSelectionRectangle: Boolean =
    true

  override def setSelection(selectionVertexes: Set[VisualVertex], selectionLinks: Set[VisualLink]): Option[AnyVisualGraph] =
    Some(
      graph.setSelection(selectionVertexes, selectionLinks)
    )


  override def setVertexSelectedState(vertex: VisualVertex, selected: Boolean): Option[AnyVisualGraph] =
    Some(
      graph.updateVertex(
        vertex.setSelected(selected)
      )
    )


  override def setLinkSelectedState(link: VisualLink, selected: Boolean): Option[AnyVisualGraph] =
    Some(
      graph.updateLink(
        link.setSelected(selected)
      )
    )
}
