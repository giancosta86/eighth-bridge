package info.gianlucacosta.eighthbridge.graphs

import info.gianlucacosta.eighthbridge.graphs.MapUtils._

abstract class GraphCore[
  V <: Vertex,
  L <: Link,
  A <: Anchors[V]
](
  val vertexes: Set[V],
  val linksByAnchors: Map[A, Set[L]]
)
  extends Graph[V, L] {
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

  protected def copy(
                      updatedVertexes: Set[V] =
                        vertexes,
                      updatedLinksByAnchors: Map[A, Set[L]] =
                        linksByAnchors
                    ): this.type

  protected def createAnchors(first: V, second: V): A

  def addVertexes(vertexesToAdd: Set[V]): this.type =
    copy(
      updatedVertexes =
        vertexes ++ vertexesToAdd
    )

  def addVertexes(vertexesToAdd: V*): this.type =
    addVertexes(vertexesToAdd.toSet)

  def addVertex(vertex: V): this.type =
    copy(
      updatedVertexes =
        vertexes + vertex
    )

  def updateVertex(updatedVertex: V): this.type =
    copy(
      updatedVertexes =
        vertexes
          .map(
            _.update(updatedVertex)
          ),

      updatedLinksByAnchors =
        linksByAnchors.map {
          case (anchors, links) =>
            anchors.update(updatedVertex) -> links
        }
    )

  def updateVertexes(updatedVertexes: Set[V]): this.type =
    updatedVertexes.foldLeft(this)((cumulatedGraph, vertex) =>
      cumulatedGraph.updateVertex(vertex)
    )
      .asInstanceOf[this.type]

  def updateVertexes(updatedVertexes: V*): this.type =
    updateVertexes(updatedVertexes.toSet)

  def removeVertexes(vertexesToRemove: Set[V]): this.type =
    copy(
      updatedVertexes =
        vertexes -- vertexesToRemove,

      updatedLinksByAnchors =
        linksByAnchors.filterKeys(anchors =>
          (anchors.toSet & vertexesToRemove).isEmpty
        )
    )

  def removeVertexes(vertexes: V*): this.type =
    removeVertexes(vertexes.toSet)

  def removeVertex(vertex: V): this.type =
    removeVertexes(Set(vertex))

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

  def getLinksBetween(anchors: A): Set[L] =
    linksByAnchors.getOrElse(
      anchors,
      Set[L]()
    )

  def getLinksBetween(first: V, second: V): Set[L] = {
    val anchors =
      createAnchors(first, second)

      getLinksBetween(anchors)
  }

  @transient
  lazy val anchorsByLink: Map[L, A] =
    linksByAnchors
      .flatMap { case (anchors, edges) =>
        edges.map(_ -> anchors)
      }

  def getAnchors(link: L): Option[A] =
    anchorsByLink.get(link)

  def addLink(anchors: A, link: L): this.type =
    if (anchors.toSet.subsetOf(vertexes))
      copy(
        updatedLinksByAnchors =
          linksByAnchors +
            (anchors -> link)
      )
    else
      this


  def addLink(first: V, second: V, link: L): this.type = {
    val anchors =
      createAnchors(first, second)

    addLink(anchors, link)
  }

  def addLink(vertexPair: (V, V), link: L): this.type = {
    val anchors =
      createAnchors(
        vertexPair._1,
        vertexPair._2
      )

    addLink(anchors, link)
  }

  def updateLink(updatedLink: L): this.type =
    copy(
      updatedLinksByAnchors =
        linksByAnchors.mapValues(
          _.update(updatedLink)
        )
    )

  def updateLinks(updatedLinks: Set[L]): this.type =
    updatedLinks.foldLeft(this)((cumulatedGraph, link) =>
      cumulatedGraph.updateLink(link)
    )
      .asInstanceOf[this.type]

  def updateLinks(updatedLinks: L*): this.type =
    updateLinks(updatedLinks.toSet)

  def removeLinks(linksToRemove: Set[L]): this.type =
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

  def removeLinks(links: L*): this.type =
    removeLinks(links.toSet)

  def removeLink(link: L): this.type =
    removeLinks(Set(link))

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

  val isCyclic: Boolean

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
