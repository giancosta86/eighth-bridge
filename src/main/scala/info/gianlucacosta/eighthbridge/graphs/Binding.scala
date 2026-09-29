package info.gianlucacosta.eighthbridge.graphs

import java.util.UUID

/**
  * Generic binding - that is, a connection between a link and any number of vertexes
  */
trait Binding extends GraphElement {
  /**
    * The set of ids of the attached vertexes
    */
  val vertexIds: Set[UUID]

  /**
    * The sorted list containing the ids of the attached vertexes.
    * The actual sort order depends on the specific binding implementation.
    */
  val sortedVertexIds: List[UUID]

  /**
    * The id of the attached link
    */
  val linkId: UUID
}
