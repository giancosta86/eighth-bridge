package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.{Graph, GraphCore, Link, Vertex}

import scala.annotation.tailrec
import scala.language.implicitConversions

object DirectedGraph {
  implicit def toDirectedGraph[
    TGraphVertex <: Vertex,
    TGraphLink <: Link,
    TDirectedGraphVertex <: TGraphVertex,
    TDirectedGraphArc <: TGraphLink with Arc
  ](self: Graph[TGraphVertex, TGraphLink]): DirectedGraph[TDirectedGraphVertex, TDirectedGraphArc] =
    self.asInstanceOf[DirectedGraph[TDirectedGraphVertex, TDirectedGraphArc]]

  implicit def toGraph[
    TGraphVertex <: Vertex,
    TGraphLink <: Link,
    TDirectedGraphVertex <: TGraphVertex,
    TDirectedGraphArc <: TGraphLink with Arc
  ](self: DirectedGraph[TDirectedGraphVertex, TDirectedGraphArc]): Graph[TGraphVertex, TGraphLink] =
    self.asInstanceOf[Graph[TGraphVertex, TGraphLink]]

  implicit def toParent[
    TParentVertex <: Vertex,
    TParentArc <: Arc,
    TDescendantVertex <: TParentVertex,
    TDescendantArc <: TParentArc
  ](descendant: DirectedGraph[TDescendantVertex, TDescendantArc]): DirectedGraph[TParentVertex, TParentArc] =
    descendant.asInstanceOf[DirectedGraph[TParentVertex, TParentArc]]
}

final class DirectedGraph[
  V <: Vertex,
  A <: Arc
] private(
   vertexes: Set[V],
   linksByAnchors: Map[DirectedAnchors[V], Set[A]]
 ) extends GraphCore[V, A, DirectedAnchors[V]](
  vertexes,
  linksByAnchors
) {
  def this() = this(
    vertexes =
      Set[V](),
    linksByAnchors =
      Map[DirectedAnchors[V], Set[A]]()
  )

  override protected def copy(
                               updatedVertexes: Set[V],
                               updatedLinksByAnchors: Map[DirectedAnchors[V], Set[A]]
                             ): this.type =
    new DirectedGraph[V, A](
      vertexes =
        updatedVertexes,
      linksByAnchors =
        updatedLinksByAnchors
    )
      .asInstanceOf[this.type]

  override protected def createAnchors(first: V, second: V): DirectedAnchors[V] =
    new DirectedAnchors[V](first, second)

  override def addLink(source: V, target: V, linkToAdd: A): this.type =
    super.addLink(source, target, linkToAdd)

  @transient
  lazy val successorsByVertex: Map[V, Set[V]] =
    linksByAnchors
      .keySet
      .groupBy(_.source)
      .mapValues(
        _.map(_.target)
      )

  /**
   * Returns the set of vertexes that are target of any link exiting the given vertex.
   */
  def getSuccessors(vertex: V): Set[V] =
    successorsByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  lazy val exitingLinksByVertex: Map[V, Set[A]] =
    linksByAnchors
      .groupBy { case (anchors, _) =>
        anchors.source
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

  @transient
  lazy val predecessorsByVertex: Map[V, Set[V]] =
    linksByAnchors
      .keySet
      .groupBy(_.target)
      .mapValues(
        _.map(_.source)
      )

  /**
   * Returns the set of vertexes that are source of any link entering the given vertex.
   */
  def getPredecessors(vertex: V): Set[V] =
    predecessorsByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  lazy val enteringLinksByVertex: Map[V, Set[A]] =
    linksByAnchors
      .view
      .groupBy { case (anchors, _) =>
        anchors.target
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
  @transient
  lazy val rootVertexes: Set[V] =
    vertexes -- enteringLinksByVertex.keySet

  /**
   * The vertexes having no exiting links
   */
  @transient
  lazy val leafVertexes: Set[V] =
    vertexes -- exitingLinksByVertex.keySet


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
        if (processedVertexes.size == vertexes.size)
          Some(cumulatedResult)
        else
          None
    }
  }

  @transient
  lazy val isCyclic: Boolean =
    fold[Unit](())(
      (_, _) => ()
    )
      .isEmpty
}
