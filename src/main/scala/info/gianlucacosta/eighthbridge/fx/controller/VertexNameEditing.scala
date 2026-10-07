package info.gianlucacosta.eighthbridge.fx.controller

import info.gianlucacosta.eighthbridge.fx.{GraphCanvasController, VisualGraph, VisualLink, VisualVertex}
import info.gianlucacosta.eighthbridge.graphs.features.Named
import info.gianlucacosta.helios.fx.dialogs.InputDialogs
import scalafx.geometry.Point2D

/**
  * Mixin for GraphCanvasController that:
  * <ul>
  * <li>Creates a vertex by assigning it a unique name based on a counter</li>
  * <li>Allows the user to edit such name, ensuring the new name is still unique</li>
  * </ul>
  *
  * @tparam V Vertex
  * @tparam L Link
  */
trait VertexNameEditing[V <: VisualVertex with Named, L <: VisualLink, G <: VisualGraph[V, L]]
  extends VertexEditing[V, L, G]
  with VertexNaming[V, L, G] {
  override protected def doEditVertex(vertex: V): Option[V] = {
    val newNameInput =
      InputDialogs.askForString("Vertex name:", vertex.name, "Edit vertex")

    if (newNameInput.isEmpty) {
      return None
    }


    val newName =
      newNameInput.get

    if (newName.isEmpty) {
      throw new IllegalArgumentException("The vertex must have a name!")
    }


    val nameAssignedToAnotherVertex =
      graph
        .vertexes
        .exists(otherVertex => otherVertex.name == newName && otherVertex.id != vertex.id)

    if (nameAssignedToAnotherVertex) {
      throw new IllegalArgumentException("The vertex name must be unique!")
    }

    val newProblemVertex =
      vertex.setName(name = newName)

    Some(newProblemVertex)
  }
}
