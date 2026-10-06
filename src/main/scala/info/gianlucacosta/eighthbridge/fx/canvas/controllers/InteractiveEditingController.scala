package info.gianlucacosta.eighthbridge.fx.canvas.controllers

import info.gianlucacosta.eighthbridge.fx.canvas.basic.BasicController
import info.gianlucacosta.eighthbridge.fx.canvas.{GraphCanvas, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.helios.fx.dialogs.Alerts
import scalafx.geometry.Point2D

/**
  * Interactive controller mixin providing full interactivity and the ability to easily edit vertexes and links
  *
  * @tparam V Vertex
  * @tparam L Link
  */
trait InteractiveEditingController[V <: VisualVertex, L <: VisualLink, G <: VisualGraph[V, L]] extends BasicController[V, L, G] {
  override def setVertexSelectedState(graph: G, vertex: V, selected: Boolean): Option[G] =
    Some(
      graph.replaceVertex(vertex.visualCopy(selected = selected))
    )


  override def setLinkSelectedState(graph: G, link: L, selected: Boolean): Option[G] =
    Some(
      graph.replaceLink(link.visualCopy(selected = selected))
    )


  override def setSelection(graph: G, selectionVertexes: Set[V], selectionLinks: Set[L]): Option[G] =
    Some(
      graph.setSelection(selectionVertexes, selectionLinks)
    )


  override def deleteSelection(graphCanvas: GraphCanvas[V, L, G], graph: G): Option[G] = {
    val selectedVertexes =
      graph.selectedVertexes

    val selectedLinks =
      graph.selectedLinks

    Some(
      graph
        .removeLinks(selectedLinks)
        .removeVertexes(selectedVertexes)
    )
  }


  override def canDrawSelectionRectangle: Boolean =
    true


  override def createLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] = {
    val newLink =
      link.visualCopy(internalPoints = newInternalPoints)

    Some(
      graph.replaceLink(newLink)
    )
  }


  override def canDragLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], oldInternalPoint: Point2D, newInternalPoint: Point2D): Boolean =
    true


  override def deleteLinkInternalPoint(graph: G, link: L, newInternalPoints: List[Point2D], internalPoint: Point2D): Option[G] = {
    val newLink =
      link.visualCopy(internalPoints = newInternalPoints)

    Some(
      graph.replaceLink(newLink)
    )
  }

  override def dragLinkLabel(graph: G, link: L, oldCenter: Point2D, newCenter: Point2D): Option[G] = {
    val newLink =
      link.visualCopy(
        labelCenter = Some(
          newCenter
        )
      )

    Some(
      graph.replaceLink(newLink)
    )
  }


  override def editVertex(graph: G, vertex: V): Option[G] = {
    while (true) {
      try {
        val editResult =
          interactiveVertexEditing(graph, vertex)

        if (editResult.isEmpty) {
          return None
        }

        val newVertex =
          editResult.get

        return Some(graph.replaceVertex(newVertex))
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
    * @param graph  The graph
    * @param vertex The vertex to edit
    * @return Some(new vertex) if the editing is complete, None if the user canceled the editing
    *
    */
  protected def interactiveVertexEditing(graph: G, vertex: V): Option[V]


  override def editLink(graph: G, link: L): Option[G] = {
    while (true) {
      try {
        val editResult =
          interactiveLinkEditing(graph, link)

        if (editResult.isEmpty) {
          return None
        }

        val newLink =
          editResult.get

        return Some(graph.replaceLink(newLink))
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
    * @param graph The graph
    * @param link  The link to edit
    * @return Some(new link) if the editing is complete, None if the user canceled the editing
    *
    */
  protected def interactiveLinkEditing(graph: G, link: L): Option[L]
}
