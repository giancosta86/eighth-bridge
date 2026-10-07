package info.gianlucacosta.eighthbridge.graphs.point2point

import info.gianlucacosta.eighthbridge.graphs.Binding

import java.util.UUID

/**
 * Binding for a point-to-point arc (that is, a directed link).
 *
 * @param id
 * @param sourceVertexId
 * @param targetVertexId
 * @param linkId
 */
case class ArcBinding(id: UUID, sourceVertexId: UUID, targetVertexId: UUID, linkId: UUID)
  extends Binding {

  override val vertexIds: Set[UUID] =
    Set(sourceVertexId, targetVertexId)
}
