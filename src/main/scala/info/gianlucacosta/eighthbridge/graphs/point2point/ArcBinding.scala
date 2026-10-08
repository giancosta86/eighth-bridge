package info.gianlucacosta.eighthbridge.graphs.point2point

import info.gianlucacosta.eighthbridge.graphs.Binding

import java.util.UUID

/**
 * Binding for a point-to-point arc (that is, a directed link).
 */
case class ArcBinding[V, L](sourceVertex: V, targetVertex: V, link: L)
  extends Binding[V, L] {

  override val vertexes: Set[V] = Set(sourceVertex, targetVertex)

  override def replaceVertex(oldVertex: V, newVertex: V): this.type =
    if (oldVertex == sourceVertex)
      copy(
        sourceVertex = newVertex
      )
    else if (oldVertex == targetVertex)
      copy(
        targetVertex = newVertex
      )
    else
      this

  override def replaceLink(oldLink: L, newLink: L): ArcBinding.this.type = {
    if (oldLink == link)
      copy(link = newLink)
    else
      this
  }
}
