package info.gianlucacosta.eighthbridge.theory

import scala.annotation.tailrec

trait DirectedGraph[V <: Vertex, A <: Arc] {
  val vertexes: Set[V]

  protected val arcsByVertexPair: Map[(V, V), Set[A]]

  //TODO! Can this be renamed to "copy"? Would the synthesized copy() method work?
  protected def graphCopy(
                           vertexes: Set[V] = vertexes,
                           arcsByVertexPair: Map[(V, V), Set[A]] = arcsByVertexPair
   ): this.type

  def addVertexes(vertexesToAdd: Set[V]): this.type =
    graphCopy(
      vertexes =
        vertexes ++ vertexesToAdd
    )

  final def addVertexes(vertexesToAdd: V*): this.type =
    addVertexes(vertexesToAdd.toSet)

  final def addVertex(vertex: V): this.type =
    graphCopy(
      vertexes =
        vertexes + vertex
    )

  def updateVertex(updatedVertex: V): this.type =
    graphCopy(
      vertexes =
        vertexes.updateVertex(updatedVertex),

      arcsByVertexPair =
        arcsByVertexPair.map {
          case (keyPair, arc) =>
            keyPair.updateVertex(updatedVertex) -> arc
        }
    )

  final def updateVertexes(updatedVertexes: Set[V]): this.type =
    updatedVertexes.foldLeft(this)((cumulatedGraph, vertex) =>
      cumulatedGraph.updateVertex(vertex)
    ).asInstanceOf[this.type]

  final def updateVertexes(updatedVertexes: V*): this.type =
    updateVertexes(updatedVertexes.toSet)

  def removeVertexes(vertexesToRemove: Set[V]): this.type =
    graphCopy(
      vertexes =
        vertexes -- vertexesToRemove,

      arcsByVertexPair =
        arcsByVertexPair.filterKeys(
          !vertexesToRemove.intersectsPair(_)
        )
    )

  final def removeVertexes(vertexes: V*): this.type =
    removeVertexes(vertexes.toSet)

  final def removeVertex(vertex: V): this.type =
    removeVertexes(Set(vertex))

  def addArc(sourceVertex: V, targetVertex: V, arc: A): this.type = {
    val vertexPair =
      sourceVertex -> targetVertex

    if (vertexes.containsPair(vertexPair)) {
      val updatedArcSet =
        arcsByVertexPair.getOrElse(vertexPair, Set()) + arc

      graphCopy(
        arcsByVertexPair =
          arcsByVertexPair + (vertexPair -> updatedArcSet)
      )
    } else
      this
  }

  final def addArc(vertexPair: (V, V), arc: A): this.type =
    addArc(vertexPair._1, vertexPair._2, arc)

  def updateArc(updatedArc: A): this.type =
    graphCopy(
      arcsByVertexPair =
        arcsByVertexPair.mapValues(_.updateArc(updatedArc))
    )

  final def updateArcs(updatedArcs: Set[A]): this.type =
    updatedArcs.foldLeft(this)((cumulatedGraph, arc) =>
      cumulatedGraph.updateArc(arc)
    ).asInstanceOf[this.type]

  final def updateArcs(updatedArcs: A*): this.type =
    updateArcs(updatedArcs.toSet)

  def removeArcs(arcsToRemove: Set[A]): this.type =
    graphCopy(
      arcsByVertexPair =
        arcsByVertexPair
          .mapValues(
            _ -- arcsToRemove
          )
          .filter { case (_, arcSet) =>
            arcSet.nonEmpty
          }
    )

  final def removeArcs(arcs: A*): this.type =
    removeArcs(arcs.toSet)

  final def removeArc(arc: A): this.type =
    removeArcs(Set(arc))

  @transient
  lazy val arcs: Set[A] =
    arcsByVertexPair
      .values
      .flatten
      .toSet

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

  @transient
  protected lazy val exitingArcsByVertex: Map[V, Set[A]] =
    arcsByVertexPair
      .flattenToSingleVertexKey { case (sourceVertex, _) =>
        sourceVertex
      }

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

  @transient
  protected lazy val enteringArcsByVertex: Map[V, Set[A]] =
    arcsByVertexPair
      .flattenToSingleVertexKey { case (_, targetVertex) =>
        targetVertex
      }

  @transient
  protected lazy val vertexPairsByArc: Map[A, (V, V)] =
    arcsByVertexPair
      .flatMap { case (vertexPair, arcs) =>
        arcs.map(_ -> vertexPair)
      }

  /**
   * Returns the set of vertexes that are target of any arc exiting the given vertex.
   */
  def getSuccessors(vertex: V): Set[V] =
    successorsByVertex.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose source is the given vertex.
   */
  def getExitingArcs(vertex: V): Set[A] =
    exitingArcsByVertex.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of vertexes that are source of any arc entering the given vertex.
   */
  def getPredecessors(vertex: V): Set[V] =
    predecessorsByVertex.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose target is the given vertex.
   */
  def getEnteringArcs(vertex: V): Set[A] =
    enteringArcsByVertex.getOrElse(
      vertex,
      Set()
    )

  def getArcsBetween(vertexPair: (V, V)): Set[A] =
    arcsByVertexPair.getOrElse(
      vertexPair,
      Set()
    )

  final def getArcsBetween(sourceVertex: V, targetVertex: V): Set[A] =
    getArcsBetween(sourceVertex -> targetVertex)

  final def getEdgesBetween(oneVertex: V, anotherVertex: V): Set[A] = {
    getArcsBetween(oneVertex -> anotherVertex) ++
      getArcsBetween(anotherVertex -> oneVertex)
  }

  def getVertexPair(arc: A): Option[(V, V)] =
    vertexPairsByArc.get(arc)

  def getAdjacent(vertex: V): Set[V] =
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
   * Topology information for a vertex in a directed graph.
   */
  case class VertexTopology(
                             predecessors: Set[V],
                             enteringArcs: Set[A],
                             exitingArcs: Set[A],
                             successors: Set[V]
                           )

  /**
   * Function passed to fold(). Its signature must include:
   * <ul>
   * <li><b>cumulatedValue</b> - the value cumulated until now</li>
   * <li><b>vertex</b> - the current vertex</li>
   * <li><b>vertexTopology</b> - describes the vertex/arc connections for the current vertex</li>
   * </ul>
   *
   * The function must return the next iteration value for <b>fold()</b>.
   */

  type VertexFoldProcessor[T] = (T, V, VertexTopology) => T


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
  def fold[T](initialValue: T)(vertexFoldProcessor: VertexFoldProcessor[T]): T = {
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
  private final def fold[T](
                             cumulatedValue: T,
                             vertexFoldProcessor: VertexFoldProcessor[T],
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
}