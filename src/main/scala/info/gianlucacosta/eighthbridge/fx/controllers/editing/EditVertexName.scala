package info.gianlucacosta.eighthbridge.fx.controllers.editing

import info.gianlucacosta.eighthbridge.core.facets.{HasName, HasNameSettable}
import info.gianlucacosta.eighthbridge.fx.VisualVertex
import info.gianlucacosta.eighthbridge.fx.controllers.primitive.EditVertex
import info.gianlucacosta.helios.fx.dialogs.InputDialogs

/**
 * Mixin for GraphCanvasController that:
 * <ul>
 * <li>Creates a vertex by assigning it a unique name based on a counter</li>
 * <li>Allows the user to edit such name, ensuring the new name is still unique</li>
 * </ul>
 */
trait EditVertexName
  extends EditVertex {

  override type V <:
    VisualVertex with HasNameSettable

  override protected def doEditVertex(vertex: V): Option[V] = {
    val newNameInput =
      InputDialogs.askForString("Vertex name:", vertex.name, "Edit vertex...")

    if (newNameInput.isEmpty) {
      return None
    }

    val newName =
      newNameInput.get

    if (newName.isEmpty) {
      throw new IllegalArgumentException("The vertex must have a name!")
    }

    val otherVertexNames =
      graph
        .vertexes
        .collect {
          case namedVertex: HasName if namedVertex != vertex =>
            namedVertex.name
        }

    //TODO: consider replacing these require() with mere IllegalArgumentException's
    require(
      !otherVertexNames.contains(newName),
      "The vertex name must be unique!"
    )

    val updatedVertex =
      vertex.setName(newName)

    Some(updatedVertex)
  }
}
