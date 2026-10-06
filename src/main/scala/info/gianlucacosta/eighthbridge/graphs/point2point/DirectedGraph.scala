package info.gianlucacosta.eighthbridge.graphs.point2point

import info.gianlucacosta.eighthbridge.graphs.{Graph, Link, Vertex}

import java.util.UUID
import scala.annotation.tailrec

/**
  * A directed graph - that is, a graph whose links are arcs.
  *
  * @tparam V Vertex type.
  * @tparam L Link type.
  */
trait DirectedGraph[V <: Vertex, L <: Link] extends Graph[V, L, ArcBinding] {
  /**
   * Adds a link from <i>sourceVertex</i> to <i>targetVertex</i>
   */
  def addLink(sourceVertex: V, targetVertex: V, link: L): this.type = {
    val binding = ArcBinding(
      id = UUID.randomUUID(),
      sourceVertexId = sourceVertex.id,
      targetVertexId = targetVertex.id,
      linkId = link.id
    )

    addLink(link, binding)
  }

  @transient
  private lazy val topologyCache: Set[(V, L, V)] =
    createTopologyCache()

  private def createTopologyCache(): Set[(V, L, V)] =
    createTopologyCache(
      Set(),
      bindings.toList
    )

  @tailrec
  private def createTopologyCache(
                                   cumulatedCache: Set[(V, L, V)],
                                   bindingsToVisit: List[ArcBinding]
                                 ): Set[(V, L, V)] = {
    bindingsToVisit match {
      case headBinding :: tailBindings =>
        val cacheEntry =
          createTopologyCacheEntry(headBinding)

        createTopologyCache(
          cumulatedCache + cacheEntry,
          tailBindings
        )

      case Nil =>
        cumulatedCache
    }
  }


  private def createTopologyCacheEntry(binding: ArcBinding): (V, L, V) = {
    val sourceVertex =
      vertexMap(binding.sourceVertexId)

    val arc =
      linkMap(binding.linkId)

    val targetVertex =
      vertexMap(binding.targetVertexId)

    (sourceVertex, arc, targetVertex)
  }

  @transient
  protected lazy val exitingVertexesMap: Map[V, Set[V]] =
    topologyCache
      .groupBy { case (sourceVertex, _, _) => sourceVertex}
      .mapValues(_.map { case (_, _, targetVertex) => targetVertex })


  @transient
  protected lazy val exitingArcsMap: Map[V, Set[L]] =
    topologyCache
      .groupBy { case (sourceVertex, _, _) => sourceVertex }
      .mapValues(_.map { case (_, arc, _) => arc })


  @transient
  private lazy val enteringVertexesMap: Map[V, Set[V]] =
    topologyCache
      .groupBy { case (_, _, targetVertex) => targetVertex }
      .mapValues(_.map { case (sourceVertex, _, _) => sourceVertex })


  @transient
  private lazy val enteringArcsMap: Map[V, Set[L]] =
    topologyCache
      .groupBy { case (_, _, targetVertex) => targetVertex }
      .mapValues(_.map { case (_, arc, _) => arc })

  @transient
  private lazy val arcsBetweenPairsMap: Map[(V, V), Set[L]] =
    topologyCache
      .groupBy { case (sourceVertex, _, targetVertex) =>
        sourceVertex -> targetVertex
      }
      .mapValues(_.map { case (_, arc, _) => arc })

  /**
   * The vertexes having no entering arcs
   */
  @transient
  lazy val rootVertexes: Set[V] =
    vertexes
      .filter(getEnteringArcs(_).isEmpty)


  /**
   * Returns the set of vertexes that are target of any arc exiting the given vertex.
   */
  def getExitingVertexes(vertex: V): Set[V] =
    exitingVertexesMap.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose source is the given vertex.
   */
  def getExitingArcs(vertex: V): Set[L] =
    exitingArcsMap.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of vertexes that are source of any arc entering the given vertex.
   */
  def getEnteringVertexes(vertex: V): Set[V] =
    enteringVertexesMap.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose target is the given vertex.
   */
  def getEnteringArcs(vertex: V): Set[L] =
    enteringArcsMap.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns all the arcs between the given vertex pair.
   *
   * @param vertexPair The (source, target) pair.
   */
  def getArcsBetween(vertexPair: (V, V)): Set[L] =
    arcsBetweenPairsMap.getOrElse(
      vertexPair,
      Set()
    )

  /**
   * Returns all the arcs starting in sourceVertex and ending in targetVertex
   *
   * @param sourceVertex The source vertex
   * @param targetVertex The target vertex
   * @return A set of links
   */
  def getArcsBetween(sourceVertex: V, targetVertex: V): Set[L] =
    getArcsBetween(sourceVertex -> targetVertex)

  override def getLinksBetween(linkVertexes: Set[V]): Set[L] = {
    require(linkVertexes.size == 2)

    val leftVertex = linkVertexes.head

    val rightVertex = linkVertexes.last

    val leftToRightArcs =
      arcsBetweenPairsMap.getOrElse(
        leftVertex -> rightVertex,
        Set()
      )

    val rightToLeftArcs =
      arcsBetweenPairsMap.getOrElse(
        rightVertex -> leftVertex,
        Set()
      )

    leftToRightArcs ++ rightToLeftArcs
  }

  override def getLinkedVertexes(vertex: V): Set[V] = {
    val enteringVertexes =
      enteringVertexesMap.getOrElse(
        vertex,
        Set()
      )

    val exitingVertexes =
      exitingVertexesMap.getOrElse(
        vertex,
        Set()
      )

    enteringVertexes ++ exitingVertexes
  }

  /**
   * Topology information for a vertex in a directed graph.
   */
  case class VertexTopology(
     enteringVertexes: Set[V],
     enteringArcs: Set[L],
     exitingArcs: Set[L],
     exitingVertexes: Set[V]
  )

  /**
    * Function passed to fold(). Its signature must include:
    * <ul>
    * <li><b>cumulatedValue</b> - the value cumulated until now</li>
    * <li><b>vertex</b> - the current vertex</li>
    * <li><b>vertexTopology</b> - a case class describing the current vertex and its entering/exiting vertexes/arcs</li>
    * </ul>
    *
    * The function must return <b>newCumulatedValue</b>, used by fold() as the return value or to call the next VertexFoldProcessor.
    *
    * @tparam T The type of the cumulated value
    */

  type VertexFoldProcessor[T] = (T, V, VertexTopology) => T


  /**
    * Takes an initial value and applies the given <b>vertexProcessor</b> to every vertex
    * in the graph, starting from the root vertexes, with the following rules:
    *
    * <ul>
    * <li>Each node will be processed <b>only</b> if all of its <i>entering vertexes</i> have been processed</li>
    *
    * <li>Any cycle in the graph will cause a CircularGraphException</li>
    * </ul>
    */
  def fold[T](initialValue: T)(vertexFoldProcessor: VertexFoldProcessor[T]): T = {
    /*
     * At the beginning of the algorithm, no vertex has been processed;
     * consequently, the fringe - i.e., the buffer of vertexes to process -
     * coincides with the root vertexes, having no entering vertexes by definition.
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
        val currentEnteringVertexes =
          enteringVertexesMap.getOrElse(currentVertex, Set())

        /*
         * If all the vertexes pointing to the current vertex have already been explored,
         * such vertex conceptually behaves now like a root vertex and can be processed.
         */
        if (currentEnteringVertexes.subsetOf(processedVertexes)) {
          val currentExitingVertexes =
            exitingVertexesMap.getOrElse(currentVertex, Set())

          val vertexTopology = VertexTopology(
            enteringVertexes = currentEnteringVertexes,
            enteringArcs =
              enteringArcsMap.getOrElse(currentVertex, Set()),
            exitingArcs =
              exitingArcsMap.getOrElse(currentVertex, Set()),
            exitingVertexes = currentExitingVertexes
          )

          val newCumulatedValue =
            vertexFoldProcessor(
              cumulatedValue,
              currentVertex,
              vertexTopology
            )

          /**
           * The next step can occur, notifying that the current vertex has been processed
           * in particular, the fringe, after losing the current node (as it's a tail),
           * can now include all the node's exiting vertexes, with no duplicates.
           */
          fold(
            newCumulatedValue,

            vertexFoldProcessor,

            processedVertexes + currentVertex,

            (fringeTail ++ currentExitingVertexes).distinct
          )
        } else {
          /*
           * If the current vertex has at least an entering vertex that was not already explored,
           * it must be removed from the fringe: it will be re-added later, after processing
           * one of its entering vertexes, as seen in the tail call above - provided the graph has no cycles.
           */
          fold(
            cumulatedValue,

            vertexFoldProcessor,

            processedVertexes,

            fringeTail
          )
        }

      /*
       * When the fringe is empty, there is no more vertex that can be considered.
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
