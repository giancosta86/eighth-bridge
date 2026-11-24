package info.gianlucacosta.eighthbridge.core

import info.gianlucacosta.eighthbridge.core.MapUtils._
import info.gianlucacosta.eighthbridge.core.directed.{Arc, DirectedAnchors, VertexFoldProcessor}
import info.gianlucacosta.eighthbridge.core.undirected.{Edge, UndirectedAnchors, UnionFind}

import scala.annotation.tailrec
import scala.language.implicitConversions

final class Graph[
  V <: Vertex,
  L <: Link
] private (
   val vertexes: Set[V],
   val linksByAnchors: Map[Anchors[V], Set[L]],
   anchorsCreator: AnchorsCreator[V]
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
        anchorsCreator
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
        (one, another) => new UndirectedAnchors[V](one, another)
    )

  def createDirected[V <: Vertex, A <: Arc](): Graph[V, A] = {
    new Graph[V, A](
      vertexes =
        Set[V](),
      linksByAnchors =
        Map[Anchors[V], Set[A]](),
      anchorsCreator =
        (source, target) => new DirectedAnchors[V](source, target)
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

  implicit class UndirectedGraphExtensions[V <: Vertex, E <: Edge](self: Graph[V, E]) {
    val isDirected: Boolean =
      false

    def isCyclic: Boolean = {
      var unionFind =
        new UnionFind[V, E](self)

      self.links
        .view
        .takeWhile(_ =>
          !unionFind.hasCycles
        )
        .foreach(link =>
          unionFind =
            unionFind.add(link)
        )

      unionFind.hasCycles
    }

    def isComplete: Boolean =
      self.vertexes
        .forall(
          self.getNeighbors(_).size == self.vertexes.size - 1
        )
  }


  implicit class DirectedGraphExtensions[V <: Vertex, A <: Arc](self: Graph[V, A]) {
    def successorsByVertex: Map[V, Set[V]] =
      self.linksByAnchors
        .keySet
        .groupBy(_.first)
        .mapValues(
          _.map(_.second)
        )

    /**
     * Returns the set of vertexes that are target of any link exiting the given vertex.
     */
    def getSuccessors(vertex: V): Set[V] =
      successorsByVertex.getOrElse(
        vertex,
        Set()
      )

    def exitingLinksByVertex: Map[V, Set[A]] =
      self.linksByAnchors
        .groupBy { case (anchors, _) =>
          anchors.first
        }
        .mapValues(
          _.flatMap({
              case (_, links) => links
            })
            .toSet
        )

    /**
     * Returns the set of links whose source is the given vertex.
     */
    def getExitingLinks(vertex: V): Set[A] =
      exitingLinksByVertex.getOrElse(
        vertex,
        Set()
      )

    def predecessorsByVertex: Map[V, Set[V]] = {
      self.linksByAnchors
        .keySet
        .groupBy(_.second)
        .mapValues(
          _.map(_.first)
        )
    }

    /**
     * Returns the set of vertexes that are source of any link entering the given vertex.
     */
    def getPredecessors(vertex: V): Set[V] =
      predecessorsByVertex.getOrElse(
        vertex,
        Set()
      )

    def enteringLinksByVertex: Map[V, Set[A]] =
      self.linksByAnchors
        .view
        .groupBy { case (anchors, _) =>
          anchors.second
        }
        .mapValues(
          _.flatMap({
              case (_, links) => links
            })
            .toSet
        )

    /**
     * Returns the set of links whose target is the given vertex.
     */
    def getEnteringLinks(vertex: V): Set[A] =
      enteringLinksByVertex.getOrElse(
        vertex,
        Set()
      )

    /**
     * The vertexes having no entering links
     */
    def rootVertexes: Set[V] =
      self.vertexes -- enteringLinksByVertex.keySet

    /**
     * The vertexes having no exiting links
     */
    def leafVertexes: Set[V] =
      self.vertexes -- exitingLinksByVertex.keySet


    /**
     * Takes an initial value and applies the given <b>VertexFoldProcessor</b> to every vertex
     * in the graph, starting from the root vertexes, with the following rules:
     *
     * <ul>
     * <li>Each node will be processed <b>only</b> if all of its <i>predecessors</i> have been processed</li>
     *
     * <li>Any cycle in the graph will immediately return None</li>
     * </ul>
     */
    def fold[T](initialValue: T)(vertexFoldProcessor: VertexFoldProcessor[T, V]): Option[T] = {
      /*
       * At the beginning of the algorithm, no vertex has been processed;
       * consequently, the fringe - i.e., the buffer of vertexes to process -
       * coincides with the root vertexes, having no predecessors by definition.
       */
      fold(
        cumulatedResult =
          initialValue,
        vertexFoldProcessor =
          vertexFoldProcessor,
        processedVertexes =
          Set(),
        fringe =
          rootVertexes.toList
      )
    }

    @tailrec
    private def fold[T](
                         cumulatedResult: T,
                         vertexFoldProcessor: VertexFoldProcessor[T, V],
                         processedVertexes: Set[V],
                         fringe: List[V]
                       ): Option[T] = {
      fringe match {
        /*
         * If the fringe contains at least one vertex to consider, the algorithm can go on.
         */
        case currentVertex :: fringeTail =>
          val currentPredecessors =
            getPredecessors(currentVertex)

          /*
           * If all the predecessors of the current vertex have already been explored,
           * such vertex conceptually behaves now like a root vertex and can be processed.
           */
          if (currentPredecessors.subsetOf(processedVertexes)) {
            val newCumulatedValue =
              vertexFoldProcessor(
                cumulatedResult,
                currentVertex
              )

            val currentSuccessors =
              getSuccessors(currentVertex)

            /*
             * The next step can occur, remembering that the current vertex has been processed
             * in particular, the fringe tail, after losing the current node,
             * can now include all the node's successors, except the ones already in the fringe.
             */
            fold(
              cumulatedResult =
                newCumulatedValue,
              vertexFoldProcessor =
                vertexFoldProcessor,
              processedVertexes =
                processedVertexes + currentVertex,
              fringe =
                (fringeTail ++ currentSuccessors).distinct
            )
          } else {
            /*
             * If the current vertex has at least a predecessor that was not already explored,
             * which is the case when a vertex is the target of links from 2 or more distinct sources,
             * it must be removed from the fringe: it will be re-added later, after processing
             * one of its predecessors, as seen in the tail call above - provided the graph has no cycles.
             */
            fold(
              cumulatedResult =
                cumulatedResult,
              vertexFoldProcessor =
                vertexFoldProcessor,
              processedVertexes =
                processedVertexes,
              fringe =
                fringeTail
            )
          }

          /*
           * When the fringe is empty, there are no more vertexes that can be considered.
           */
        case Nil =>
          /*
           * If all the vertexes in the graph have been explored, the algorithm has succeeded;
           * otherwise, it means that we are stuck in a cycle.
           */
          if (processedVertexes.size == self.vertexes.size)
            Some(cumulatedResult)
          else
            None
      }
    }

    def isCyclic: Boolean =
      fold[Unit](())(
        (_, _) => ()
      )
        .isEmpty
  }
}