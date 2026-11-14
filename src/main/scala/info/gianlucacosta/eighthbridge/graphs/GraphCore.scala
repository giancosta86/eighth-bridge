package info.gianlucacosta.eighthbridge.graphs

import info.gianlucacosta.eighthbridge.graphs.MapUtils._

abstract class GraphCore[
  V <: Vertex,
  L <: Link,
  TAnchors <: Anchors[V]
](
  override val vertexes: Set[V],
  val linksByAnchors: Map[TAnchors, Set[L]]
)
  extends Graph[V, L] {
  @transient
  override lazy val vertexesById: Map[Vertex#Id, V] =
    vertexes
      .map(vertex =>
        vertex.id -> vertex
      )
      .toMap

  override def getVertexById(id: Vertex#Id): Option[V] =
    vertexesById.get(id)

  @transient
  override lazy val isEmpty: Boolean =
    vertexes.isEmpty

  @transient
  override lazy val nonEmpty: Boolean =
    vertexes.nonEmpty

  protected def copy(
                      updatedVertexes: Set[V] =
                        vertexes,
                      updatedLinksByAnchors: Map[TAnchors, Set[L]] =
                        linksByAnchors
                    ): this.type

  protected def createAnchors(first: V, second: V): TAnchors

  override def addVertexes(vertexesToAdd: Set[V]): this.type =
    copy(
      updatedVertexes =
        vertexes ++ vertexesToAdd
    )

  override def addVertexes(vertexesToAdd: V*): this.type =
    addVertexes(vertexesToAdd.toSet)

  override def addVertex(vertexToAdd: V): this.type =
    copy(
      updatedVertexes =
        vertexes + vertexToAdd
    )

  override def updateVertex(updatedVertex: V): this.type =
    copy(
      updatedVertexes =
        vertexes.update(updatedVertex),

      updatedLinksByAnchors =
        linksByAnchors.map {
          case (anchors, links) =>
            anchors.update(updatedVertex) -> links
        }
    )

  override def updateVertexes(updatedVertexes: Set[V]): this.type =
    updatedVertexes.foldLeft(this)((cumulatedGraph, vertex) =>
      cumulatedGraph.updateVertex(vertex)
    )
      .asInstanceOf[this.type]

  override def updateVertexes(updatedVertexes: V*): this.type =
    updateVertexes(updatedVertexes.toSet)

  override def removeVertexes(vertexesToRemove: Set[V]): this.type =
    copy(
      updatedVertexes =
        vertexes -- vertexesToRemove,

      updatedLinksByAnchors =
        linksByAnchors.filterKeys(anchors =>
          (anchors.toSet & vertexesToRemove).isEmpty
        )
    )

  override def removeVertexes(vertexesToRemove: V*): this.type =
    removeVertexes(vertexesToRemove.toSet)

  override def removeVertex(vertexToRemove: V): this.type =
    removeVertexes(Set(vertexToRemove))

  @transient
  override lazy val links: Set[L] =
    linksByAnchors
      .flatMap(_._2)
      .toSet

  @transient
  override lazy val linksById: Map[Link#Id, L] =
    links
      .map(link =>
        link.id -> link
      )
      .toMap

  override def getLinkById(id: Link#Id): Option[L] =
    linksById.get(id)

  def getLinksBetween(anchors: TAnchors): Set[L] =
    linksByAnchors.getOrElse(
      anchors,
      Set[L]()
    )

  override def getLinksBetween(firstAnchor: V, secondAnchor: V): Set[L] = {
    val anchors =
      createAnchors(firstAnchor, secondAnchor)

    getLinksBetween(anchors)
  }

  override def getAnchorPair(link: L): Option[(V, V)] =
    anchorsByLink
      .get(link)
      .map(_.toPair)

  @transient
  lazy val anchorsByLink: Map[L, TAnchors] =
    linksByAnchors
      .flatMap { case (anchors, edges) =>
        edges.map(_ -> anchors)
      }

  def getAnchors(link: L): Option[TAnchors] =
    anchorsByLink.get(link)

  def addLink(anchors: TAnchors, link: L): this.type =
    if (anchors.toSet.subsetOf(vertexes))
      copy(
        updatedLinksByAnchors =
          linksByAnchors.safeAdd(anchors -> link)
      )
    else
      this

  override def addLink(firstAnchor: V, secondAnchor: V, linkToAdd: L): this.type = {
    val anchors =
      createAnchors(firstAnchor, secondAnchor)

    addLink(anchors, linkToAdd)
  }

  override def addLink(anchorPair: (V, V), linkToAdd: L): this.type = {
    val anchors =
      createAnchors(
        anchorPair._1,
        anchorPair._2
      )

    addLink(anchors, linkToAdd)
  }

  override def updateLink(updatedLink: L): this.type =
    copy(
      updatedLinksByAnchors =
        linksByAnchors.mapValues(
          _.update(updatedLink)
        )
    )

  override def updateLinks(updatedLinks: Set[L]): this.type =
    updatedLinks.foldLeft(this)((cumulatedGraph, link) =>
      cumulatedGraph.updateLink(link)
    )
      .asInstanceOf[this.type]

  override def updateLinks(updatedLinks: L*): this.type =
    updateLinks(updatedLinks.toSet)

  override def removeLinks(linksToRemove: Set[L]): this.type =
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

  override def removeLinks(linksToRemove: L*): this.type =
    removeLinks(linksToRemove.toSet)

  override def removeLink(linkToRemove: L): this.type =
    removeLinks(Set(linkToRemove))

  @transient
  override lazy val neighborsByVertex: Map[V, Set[V]] =
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

  override def getNeighbors(vertex: V): Set[V] =
    neighborsByVertex.getOrElse(
      vertex,
      Set[V]()
    )

  @transient
  override lazy val connectedVertexes: Set[V] =
    neighborsByVertex.keySet

  @transient
  override lazy val isolatedVertexes: Set[V] =
    vertexes -- connectedVertexes

  @transient
  override lazy val connectedLinksByVertex: Map[V, Set[L]] =
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

  override def getConnectedLinks(vertex: V): Set[L] =
    connectedLinksByVertex.getOrElse(
      vertex,
      Set[L]()
    )

  override val isCyclic: Boolean

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
}
