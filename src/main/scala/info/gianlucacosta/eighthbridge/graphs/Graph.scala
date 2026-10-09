package info.gianlucacosta.eighthbridge.graphs

import java.util.UUID

/**
 * A general-purpose, read-only graph.
 *
 * Every status-changing operation returns a new graph.
 *
 * @tparam V Vertex type.
 * @tparam L Link type.
 */
trait Graph[V, L, B <: Binding[V, L]] {
  def vertexes: Set[V]

  def bindings: Set[B]

  @transient
  final lazy val links: Set[L] = bindings.map(_.link)


  protected def graphCopy(
                           vertexes: Set[V] = vertexes,
                           bindings: Set[B] = bindings
                         ): this.type

  def addVertexes(vertexesToAdd: Set[V]): this.type =
    graphCopy(
      vertexes = vertexes ++ vertexesToAdd
    )

  final def addVertexes(vertexesToAdd: V*): this.type =
    addVertexes(vertexesToAdd.toSet)

  final def addVertex(vertex: V): this.type =
    addVertexes(Set(vertex))

  def replaceVertex(oldVertex: V, newVertex: V): this.type = {
    if (vertexes.contains(oldVertex))
      graphCopy(
        vertexes = vertexes - oldVertex + newVertex,
        bindings = bindings.map(_.replaceVertex(oldVertex, newVertex))
      )
    else
      this
  }

  def removeVertexes(vertexesToRemove: Set[V]): this.type =
    graphCopy(
      vertexes = vertexes -- vertexesToRemove,
      bindings = bindings.filter(binding =>
        (binding.vertexes & vertexesToRemove).isEmpty
      )
    )

  final def removeVertexes(vertexes: V*): this.type =
    removeVertexes(vertexes.toSet)

  final def removeVertex(vertex: V): this.type =
    removeVertexes(Set(vertex))

  protected def addBinding(binding: B): this.type = {
    require(!links.contains(binding.link))

    graphCopy(
      bindings = bindings + binding
    )
  }

  def replaceLink(oldLink: L, newLink: L): this.type =
    graphCopy(
      bindings = bindings.map(_.replaceLink(oldLink, newLink))
    )

  def removeLinks(linksToRemove: Set[L]): this.type =
    graphCopy(
      bindings = bindings.filter(binding =>
        !linksToRemove.contains(binding.link)
      )
    )

  final def removeLinks(linksToRemove: L*): this.type =
    removeLinks(linksToRemove.toSet)

  final def removeLink(linkToRemove: L): this.type =
    removeLinks(Set(linkToRemove))


  @transient
  lazy val linkedVertexes: Set[V] =
    bindings.flatMap(_.vertexes)

  @transient
  lazy val unlinkedVertexes: Set[V] =
    vertexes -- linkedVertexes


  def getLinksBetween(linkVertexes: Set[V]): Set[L] = {
    bindings
      .view
      .filter(_.vertexes == linkVertexes)
      .map(_.link)
      .toSet
  }

  final def getLinksBetween(linkVertexes: V*): Set[L] =
    getLinksBetween(linkVertexes.toSet)


  def getLinkedVertexes(vertex: V): Set[V] =
    bindings
      .view
      .filter(_.vertexes.contains(vertex))
      .flatMap(_.vertexes - vertex)
      .toSet

  def getAnchorVertexes(link: L): Set[V] =
    bindings
      .view
      .find(_.link == link)
      .map(_.vertexes)
      .getOrElse(Set())

  def map(
           vertexMapper: V => V,
           linkMapper: L => L
         ): this.type =
    graphCopy(
      vertexes = vertexes.map(vertexMapper),

      bindings = bindings.map(binding =>
        binding.replaceLink(binding.link, linkMapper(binding.link))
      )
    )

  final def mapVertexes(vertexMapper: V => V): this.type =
    map(
      vertexMapper,
      identity
    )

  final def mapLinks(linkMapper: L => L): this.type =
    map(
      identity,
      linkMapper
    )
}
