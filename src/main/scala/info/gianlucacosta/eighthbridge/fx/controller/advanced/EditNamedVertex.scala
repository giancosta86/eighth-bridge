package info.gianlucacosta.eighthbridge.fx.controller.advanced

import info.gianlucacosta.eighthbridge.fx.controller.primitive.EditVertex
import info.gianlucacosta.eighthbridge.fx.graph.VisualVertex
import info.gianlucacosta.eighthbridge.graphs.features.Named
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
  * Mixin for GraphCanvasController that:
  * <ul>
  * <li>Creates a vertex by assigning it a unique name based on a counter</li>
  * <li>Allows the user to edit such name, ensuring the new name is still unique</li>
  * </ul>
  */
trait EditNamedVertex
  extends EditVertex {
  override type V <:
    VisualVertex with Named

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
        .exists(otherVertex => otherVertex.asInstanceOf[Named].name == newName && otherVertex != vertex)

    require(
      !nameAssignedToAnotherVertex,
      "The vertex name must be unique!"
    )

    val updatedVertex =
      vertex.setName(newName)

    Some(updatedVertex)
  }
}
