package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.undirected.UndirectedGraph
import info.gianlucacosta.eighthbridge.graphs.{CircularGraphException, GraphCore, Link, Vertex}

import scala.annotation.tailrec

final class DirectedGraph[V <: Vertex, A <: Link] private(
                                                     vertexes: Set[V],
                                                     private val arcsByVertexPair: Map[(V, V), Set[A]]
                                                   )
  extends GraphCore[V, A](vertexes) {

  def this() = this(
    vertexes =
      Set[V](),

    arcsByVertexPair =
      Map[(V, V), Set[A]]()
  )

  override protected def setVertexes(updatedVertexes: Set[V]): this.type =
    new DirectedGraph[V, A](
      vertexes =
        updatedVertexes,

      arcsByVertexPair =
        arcsByVertexPair
    )
      .asInstanceOf[this.type]

  def updateVertex(updatedVertex: V): this.type =
    new DirectedGraph[V, A](
      vertexes =
        vertexes.update(updatedVertex),

      arcsByVertexPair =
        arcsByVertexPair.map {
          case (keyPair, arc) =>
            keyPair.update(updatedVertex) -> arc
        }
    )
      .asInstanceOf[this.type]

  def removeVertexes(vertexesToRemove: Set[V]): this.type =
    new DirectedGraph[V, A](
      vertexes =
        vertexes -- vertexesToRemove,

      arcsByVertexPair =
        arcsByVertexPair.filterKeys(
          !vertexesToRemove.intersectsPair(_)
        )
    )
      .asInstanceOf[this.type]

  def addArc(sourceVertex: V, targetVertex: V, arc: A): DirectedGraph[V, A] = {
    val vertexPair =
      sourceVertex -> targetVertex

    if (vertexes.containsPair(vertexPair)) {
      val updatedArcSet =
        arcsByVertexPair.getOrElse(vertexPair, Set()) + arc

      new DirectedGraph[V, A](
        vertexes =
          vertexes,
        arcsByVertexPair =
          arcsByVertexPair + (vertexPair -> updatedArcSet)
      )
    } else
      this
  }

  def addArc(vertexPair: (V, V), arc: A): DirectedGraph[V, A] =
    addArc(vertexPair._1, vertexPair._2, arc)

  def updateArc(updatedArc: A): DirectedGraph[V, A] =
    new DirectedGraph[V, A](
      vertexes =
        vertexes,
      arcsByVertexPair =
        arcsByVertexPair.mapValues(_.update(updatedArc))
    )

  def updateArcs(updatedArcs: Set[A]): DirectedGraph[V, A] =
    updatedArcs.foldLeft(this)((cumulatedGraph, arc) =>
      cumulatedGraph.updateArc(arc)
    )

  def updateArcs(updatedArcs: A*): DirectedGraph[V, A] =
    updateArcs(updatedArcs.toSet)

  def removeArcs(arcsToRemove: Set[A]): DirectedGraph[V, A] =
    new  DirectedGraph[V, A](
      vertexes =
        vertexes,
      arcsByVertexPair =
        arcsByVertexPair
          .mapValues(
            _ -- arcsToRemove
          )
          .filter { case (_, arcSet) =>
            arcSet.nonEmpty
          }
    )

  def removeArcs(arcs: A*): DirectedGraph[V, A] =
    removeArcs(arcs.toSet)

  def removeArc(arc: A): DirectedGraph[V, A] =
    removeArcs(Set(arc))

  @transient
  lazy val arcs: Set[A] =
    arcsByVertexPair
      .values
      .flatten
      .toSet

  @transient
  protected lazy val arcsById: Map[Link#EntityId, A] =
    arcs
      .map(arc => arc.id -> arc)
      .toMap

  def getArcById(id: Link#EntityId): Option[A] =
    arcsById.get(id)


  @transient
  protected lazy val successorsByVertex: Map[V, Set[V]] =
    arcsByVertexPair
      .keySet
      .groupBy {
        case (sourceVertex, _) =>
          sourceVertex
      }
      .mapValues(
        _.map { case (_, targetVertex ) =>
          targetVertex
        }
      )

  /**
   * Returns the set of vertexes that are target of any arc exiting the given vertex.
   */
  def getSuccessors(vertex: V): Set[V] =
    successorsByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  protected lazy val exitingArcsByVertex: Map[V, Set[A]] =
    arcsByVertexPair
      .flattenToSingleVertexKey { case (sourceVertex, _) =>
        sourceVertex
      }

  /**
   * Returns the set of arcs whose source is the given vertex.
   */
  def getExitingArcs(vertex: V): Set[A] =
    exitingArcsByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  protected lazy val predecessorsByVertex: Map[V, Set[V]] =
    arcsByVertexPair
      .keySet
      .groupBy {
        case (_, targetVertex) =>
          targetVertex
      }
      .mapValues(
        _.map { case (sourceVertex, _ ) =>
          sourceVertex
        }
      )

  /**
   * Returns the set of vertexes that are source of any arc entering the given vertex.
   */
  def getPredecessors(vertex: V): Set[V] =
    predecessorsByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  protected lazy val enteringArcsByVertex: Map[V, Set[A]] =
    arcsByVertexPair
      .flattenToSingleVertexKey { case (_, targetVertex) =>
        targetVertex
      }

  /**
   * Returns the set of arcs whose target is the given vertex.
   */
  def getEnteringArcs(vertex: V): Set[A] =
    enteringArcsByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  protected lazy val vertexPairByArc: Map[A, (V, V)] =
    arcsByVertexPair
      .flatMap { case (vertexPair, arcs) =>
        arcs.map(_ -> vertexPair)
      }

  def getAnchors(arc: A): Option[(V, V)] =
    vertexPairByArc.get(arc)


  def getArcsBetween(vertexPair: (V, V)): Set[A] =
    arcsByVertexPair.getOrElse(
      vertexPair,
      Set()
    )

  def getArcsBetween(sourceVertex: V, targetVertex: V): Set[A] =
    getArcsBetween(sourceVertex -> targetVertex)

  def getNeighbors(vertex: V): Set[V] =
    getSuccessors(vertex) ++ getPredecessors(vertex)

  @transient
  lazy val connectedVertexes: Set[V] =
    exitingArcsByVertex.keySet ++ enteringArcsByVertex.keySet

  @transient
  lazy val isolatedVertexes: Set[V] =
    vertexes -- connectedVertexes

  /**
   * The vertexes having no entering arcs
   */
  @transient
  lazy val rootVertexes: Set[V] =
    vertexes -- enteringArcsByVertex.keySet

  /**
   * The vertexes having no exiting arcs
   */
  @transient
  lazy val leafVertexes: Set[V] =
    vertexes -- exitingArcsByVertex.keySet


  /**
   * Takes an initial value and applies the given <b>VertexFoldProcessor</b> to every vertex
   * in the graph, starting from the root vertexes, with the following rules:
   *
   * <ul>
   * <li>Each node will be processed <b>only</b> if all of its <i>predecessors</i> have been processed</li>
   *
   * <li>Any cycle in the graph will cause a CircularGraphException</li>
   * </ul>
   */
  def fold[T](initialValue: T)(vertexFoldProcessor: VertexFoldProcessor[T, V, A]): T = {
    /*
     * At the beginning of the algorithm, no vertex has been processed;
     * consequently, the fringe - i.e., the buffer of vertexes to process -
     * coincides with the root vertexes, having no predecessors by definition.
     */
    fold(
      initialValue,
      vertexFoldProcessor,
      Set(),
      rootVertexes.toList
    )
  }

  @tailrec
  private def fold[T](
                             cumulatedValue: T,
                             vertexFoldProcessor: VertexFoldProcessor[T, V, A],
                             processedVertexes: Set[V],
                             fringe: List[V]
                           ): T = {
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
          val currentSuccessors =
            getSuccessors(currentVertex)

          val vertexTopology =
            VertexTopology(
              predecessors =
                currentPredecessors,

              enteringArcs =
                getEnteringArcs(currentVertex),

              exitingArcs =
                getExitingArcs(currentVertex),

              successors =
                currentSuccessors
            )

          val newCumulatedValue =
            vertexFoldProcessor(
              cumulatedValue,
              currentVertex,
              vertexTopology
            )

          /**
           * The next step can occur, notifying that the current vertex has been processed
           * in particular, the fringe tail, after losing the current node,
           * can now include all the node's successors, except the ones already in the fringe.
           */
          fold(
            newCumulatedValue,

            vertexFoldProcessor,

            processedVertexes + currentVertex,

            (fringeTail ++ currentSuccessors).distinct
          )
        } else {
          /*
           * If the current vertex has at least a predecessor that was not already explored,
           * which is the case when a vertex is the target of arcs from 2 or more distinct sources,
           * it must be removed from the fringe: it will be re-added later, after processing
           * one of its predecessors, as seen in the tail call above - provided the graph has no cycles.
           */
          fold(
            cumulatedValue,

            vertexFoldProcessor,

            processedVertexes,

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
          cumulatedValue
        else
          throw new CircularGraphException
    }
  }

  def toUndirected: UndirectedGraph[V, A] = {
    var result =
      new UndirectedGraph[V, A]()

    vertexes.foreach(vertex =>
      result =
        result.addVertex(vertex)
    )

    arcsByVertexPair.foreach { case ((sourceVertex, targetVertex), arcs) =>
      arcs.foreach(arc =>
        result =
          result.addEdge(sourceVertex, targetVertex, arc)
      )
    }

    result
  }

  override def links: Set[A] =
    arcs

  override def getLinkById(id: Int): Option[A] =
    getArcById(id)
}
