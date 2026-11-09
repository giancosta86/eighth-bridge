package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.undirected.UndirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{GraphCore, Link, Vertex}

import scala.annotation.tailrec

final class DirectedGraph[
  V <: Vertex,
  L <: Link
] private(
   vertexes: Set[V],
   linksByAnchors: Map[DirectedAnchors[V], Set[L]]
 ) extends GraphCore[V, L, DirectedAnchors[V]](
  vertexes,
  linksByAnchors
) {
  def this() = this(
    vertexes =
      Set[V](),
    linksByAnchors =
      Map[DirectedAnchors[V], Set[L]]()
  )

  override protected def copy(
                               updatedVertexes: Set[V],
                               updatedLinksByAnchors: Map[DirectedAnchors[V], Set[L]]
                             ): this.type =
    new DirectedGraph[V, L](
      vertexes =
        updatedVertexes,
      linksByAnchors =
        updatedLinksByAnchors
    )
      .asInstanceOf[this.type]

  override protected def createAnchors(first: V, second: V): DirectedAnchors[V] =
    new DirectedAnchors[V](first, second)

  override def addLink(source: V, target: V, link: L): this.type =
    super.addLink(source, target, link)

  @transient
  lazy val successorsByVertex: Map[V, Set[V]] =
    linksByAnchors
      .keySet
      .map(_.toPair)
      .groupBy {
        case (sourceVertex, _) =>
          sourceVertex
      }
      .mapValues(
        _.map { case (_, targetVertex) =>
          targetVertex
        }
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
  lazy val exitingLinksByVertex: Map[V, Set[L]] =
    linksByAnchors
      .view
      .groupBy { case (anchors, _) =>
        anchors
          .toPair
          ._1
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
  def getExitingLinks(vertex: V): Set[L] =
    exitingLinksByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  lazy val predecessorsByVertex: Map[V, Set[V]] =
    linksByAnchors
      .keySet
      .map(_.toPair)
      .groupBy {
        case (_, targetVertex) =>
          targetVertex
      }
      .mapValues(
        _.map { case (sourceVertex, _) =>
          sourceVertex
        }
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
  lazy val enteringLinksByVertex: Map[V, Set[L]] =
    linksByAnchors
      .view
      .groupBy { case (anchors, _) =>
        anchors
          .toPair
          ._2
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
  def getEnteringLinks(vertex: V): Set[L] =
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

  def toUndirected: UndirectedGraph[V, L] = {
    var result =
      new UndirectedGraph[V, L]

    vertexes.foreach(vertex =>
      result =
        result.addVertex(vertex)
    )

    linksByAnchors.foreach { case (anchors, links) =>
      val (sourceVertex, targetVertex) =
        anchors.toPair

      links.foreach(link =>
        result =
          result.addLink(sourceVertex, targetVertex, link)
      )
    }

    result
  }
}
