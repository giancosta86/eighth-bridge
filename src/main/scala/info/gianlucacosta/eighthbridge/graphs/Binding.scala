package info.gianlucacosta.eighthbridge.graphs

import java.util.UUID

/**
 * Generic binding - that is, a connection between a link and any number of vertexes.
 */
trait Binding extends GraphElement {
  /**
   * The set of ids of the anchor vertexes.
   */
  val vertexIds: Set[UUID]

  /**
   * The id of the attached link.
   */
  val linkId: UUID
}
