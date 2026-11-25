package info.gianlucacosta.eighthbridge.core.directed

import info.gianlucacosta.eighthbridge.core.{Graph, Vertex}

import scala.annotation.tailrec

trait DirectedGraphExtensionOps[V <: Vertex, A <: Arc] {
  val self: Graph[V, A]

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
