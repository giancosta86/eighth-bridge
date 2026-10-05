package info.gianlucacosta.eighthbridge.graphs.point2point

import java.util.UUID

import info.gianlucacosta.eighthbridge.graphs.Binding

/**
  * Binding for a point-to-point edge (that is, an undirected link)
  *
  * @param id
  * @param vertexIds
  * @param linkId
  */
case class EdgeBinding(id: UUID, vertexIds: Set[UUID], linkId: UUID)
  extends Binding {

  /**
   * The order, in the case of an edge,
   */
  override val orderedVertexIds: List[UUID] =
    vertexIds.toList.sorted
}
