package info.gianlucacosta.eighthbridge.core

import info.gianlucacosta.eighthbridge.core.MapUtils._
import info.gianlucacosta.eighthbridge.core.directed.{Arc, DirectedAnchors, DirectedGraphExtensionOps, VertexFoldProcessor}
import info.gianlucacosta.eighthbridge.core.undirected.{Edge, UndirectedAnchors, UndirectedGraphExtensionOps, UnionFind}

import scala.annotation.tailrec
import scala.language.implicitConversions

final class Graph[
  V <: Vertex,
  L <: Link
] private (
   val vertexes: Set[V],
   val linksByAnchors: Map[Anchors[V], Set[L]],
   anchorsCreator: AnchorsCreator[V],
   val isDirected: Boolean
 ) {
  @transient
  lazy val vertexesById: Map[Vertex#Id, V] =
    vertexes
      .map(vertex =>
        vertex.id -> vertex
      )
      .toMap

  def getVertexById(id: Vertex#Id): Option[V] =
    vertexesById.get(id)

  @transient
  lazy val isEmpty: Boolean =
    vertexes.isEmpty

  @transient
  lazy val nonEmpty: Boolean =
    vertexes.nonEmpty

  private def copy(
                  updatedVertexes: Set[V] =
                    vertexes,
                  updatedLinksByAnchors: Map[Anchors[V], Set[L]] =
                    linksByAnchors
                  ): Graph[V, L] =
    new Graph(
      vertexes =
        updatedVertexes,
      linksByAnchors =
        updatedLinksByAnchors,
      anchorsCreator =
        anchorsCreator,
      isDirected =
        isDirected
    )

  def addVertexes(vertexesToAdd: Set[V]): Graph[V, L] =
    copy(
      updatedVertexes =
        vertexes ++ vertexesToAdd
    )

  def addVertexes(vertexesToAdd: V*): Graph[V, L] =
    addVertexes(vertexesToAdd.toSet)

  def addVertex(vertexToAdd: V): Graph[V, L] =
    copy(
      updatedVertexes =
        vertexes + vertexToAdd
    )

  def updateVertex(updatedVertex: V): Graph[V, L] =
    copy(
      updatedVertexes =
        vertexes.update(updatedVertex),
      updatedLinksByAnchors =
        linksByAnchors.map {
          case (anchors, links) =>
            anchors.update(updatedVertex) -> links
        }
    )

  def updateVertexes(updatedVertexes: Set[V]): Graph[V, L] =
    updatedVertexes.foldLeft(this)((cumulatedGraph, vertex) =>
      cumulatedGraph.updateVertex(vertex)
    )

  def updateVertexes(updatedVertexes: V*): Graph[V, L] =
    updateVertexes(updatedVertexes.toSet)

  def removeVertexes(vertexesToRemove: Set[V]): Graph[V, L] =
    copy(
      updatedVertexes =
        vertexes -- vertexesToRemove,
      updatedLinksByAnchors =
        linksByAnchors.filterKeys(anchors =>
          (anchors.toSet & vertexesToRemove).isEmpty
        )
    )

  def removeVertexes(vertexesToRemove: V*): Graph[V, L] =
    removeVertexes(vertexesToRemove.toSet)

  def removeVertex(vertexToRemove: V): Graph[V, L] =
    removeVertexes(Set(vertexToRemove))

  @transient
  lazy val links: Set[L] =
    linksByAnchors
      .flatMap(_._2)
      .toSet

  @transient
  lazy val linksById: Map[Link#Id, L] =
    links
      .map(link =>
        link.id -> link
      )
      .toMap

  def getLinkById(id: Link#Id): Option[L] =
    linksById.get(id)

  def getLinksBetween(anchors: Anchors[V]): Set[L] =
    linksByAnchors.getOrElse(
      anchors,
      Set[L]()
    )

  def getLinksBetween(firstAnchor: V, secondAnchor: V): Set[L] = {
    val anchors =
      anchorsCreator(firstAnchor, secondAnchor)

    getLinksBetween(anchors)
  }

  def getAnchorPair(link: L): Option[(V, V)] =
    anchorsByLink
      .get(link)
      .map(_.toPair)

  @transient
  lazy val anchorsByLink: Map[L, Anchors[V]] =
    linksByAnchors
      .flatMap { case (anchors, edges) =>
        edges.map(_ -> anchors)
      }

  def getAnchors(link: L): Option[Anchors[V]] =
    anchorsByLink.get(link)

  def addLink(anchors: Anchors[V], link: L): Graph[V, L] =
    if (anchors.toSet.subsetOf(vertexes))
      copy(
        updatedLinksByAnchors =
          linksByAnchors.safeAdd(anchors -> link)
      )
    else
      this

  def addLink(firstAnchor: V, secondAnchor: V, linkToAdd: L): Graph[V, L] = {
    val anchors =
      anchorsCreator(firstAnchor, secondAnchor)

    addLink(anchors, linkToAdd)
  }

  def addLink(anchorPair: (V, V), linkToAdd: L): Graph[V, L] = {
    val anchors =
      anchorsCreator(
        anchorPair._1,
        anchorPair._2
      )

    addLink(anchors, linkToAdd)
  }

  def updateLink(updatedLink: L): Graph[V, L] =
    copy(
      updatedLinksByAnchors =
        linksByAnchors.mapValues(
          _.update(updatedLink)
        )
    )

  def updateLinks(updatedLinks: Set[L]): Graph[V, L] =
    updatedLinks.foldLeft(this)((cumulatedGraph, link) =>
        cumulatedGraph.updateLink(link)
      )

  def updateLinks(updatedLinks: L*): Graph[V, L] =
    updateLinks(updatedLinks.toSet)

  def removeLinks(linksToRemove: Set[L]): Graph[V, L] =
    copy(
      updatedLinksByAnchors =
        linksByAnchors
          .mapValues(
            _ -- linksToRemove
          )
          .filter { case (_, links) =>
            links.nonEmpty
          }
    )

  def removeLinks(linksToRemove: L*): Graph[V, L] =
    removeLinks(linksToRemove.toSet)

  def removeLink(linkToRemove: L): Graph[V, L] =
    removeLinks(Set(linkToRemove))

  @transient
  lazy val neighborsByVertex: Map[V, Set[V]] =
    linksByAnchors
      .keys
      .flatMap(anchors => {
        val vertexPair =
          anchors.toPair

        Set(
          vertexPair,
          vertexPair.swap
        )
      })
      .groupBy {
        case (first, _) =>
          first
      }
      .mapValues(
        _.map {
            case (_, second) =>
              second
          }
          .toSet
      )

  def getNeighbors(vertex: V): Set[V] =
    neighborsByVertex.getOrElse(
      vertex,
      Set[V]()
    )

  @transient
  lazy val connectedVertexes: Set[V] =
    neighborsByVertex.keySet

  @transient
  lazy val isolatedVertexes: Set[V] =
    vertexes -- connectedVertexes

  @transient
  lazy val connectedLinksByVertex: Map[V, Set[L]] =
    linksByAnchors
      .toList
      .flatMap {
        case (anchors, link) =>
          val vertexPair =
            anchors.toPair

          Set(
            vertexPair._1 -> link,
            vertexPair._2 -> link
          )
      }
      .groupBy(_._1)
      .mapValues(
        _
          .flatMap(_._2)
          .toSet
      )

  def getConnectedLinks(vertex: V): Set[L] =
    connectedLinksByVertex.getOrElse(
      vertex,
      Set[L]()
    )

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: AnyRef =>
        eq(other)

      case _ =>
        false
    }
  }

  override def hashCode(): Int =
    System.identityHashCode(this)

  override def toString: String =
    s"<V: ${vertexes.size}; L: ${links.size}>"
}

object Graph {
  def createUndirected[V <: Vertex, E <: Edge](): Graph[V, E] =
    new Graph[V, E](
      vertexes =
        Set[V](),
      linksByAnchors =
        Map[Anchors[V], Set[E]](),
      anchorsCreator =
        (one, another) => new UndirectedAnchors[V](one, another),
      isDirected =
        false
    )

  def createDirected[V <: Vertex, A <: Arc](): Graph[V, A] = {
    new Graph[V, A](
      vertexes =
        Set[V](),
      linksByAnchors =
        Map[Anchors[V], Set[A]](),
      anchorsCreator =
        (source, target) => new DirectedAnchors[V](source, target),
      isDirected =
        true
    )
  }

  implicit def autoUpcast[
    ParentVertex <: Vertex,
    ParentLink <: Link,
    ChildVertex <: ParentVertex,
    ChildLink <: ParentLink](
       self: Graph[ChildVertex, ChildLink]
   ): Graph[ParentVertex, ParentLink] =
    self.asInstanceOf[Graph[ParentVertex, ParentLink]]

  implicit class GraphExtensions[V <: Vertex, L <: Link](self: Graph[V, L]) {
    def downcast[ChildVertex <: V, ChildLink <: Link]: Graph[ChildVertex, ChildLink] =
      self.asInstanceOf[Graph[ChildVertex, ChildLink]]
  }

  implicit class UndirectedGraphExtensions[V <: Vertex, E <: Edge](val self: Graph[V, E])
    extends UndirectedGraphExtensionOps[V, E]


  implicit class DirectedGraphExtensions[V <: Vertex, A <: Arc](val self: Graph[V, A])
    extends DirectedGraphExtensionOps[V, A]
}