package info.gianlucacosta.eighthbridge.graphs

import java.util.UUID

/**
 * Generic binding - that is, a connection between a link and any number of vertexes.
 *
 * @tparam V Vertex type.
 * @tparam L Link type.
 */
trait Binding[V, L] {
  val vertexes: Set[V]

  val link: L

  def replaceVertex(oldVertex: V, newVertex: V): this.type

  def replaceLink(oldLink: L, newLink: L): this.type
}
