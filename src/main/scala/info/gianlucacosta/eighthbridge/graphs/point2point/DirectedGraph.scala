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
    * Function passed to fold(). Its signature must be:
    * (cumulatedValue, currentEnteringArcs, currentVertex, currentExitingArcs, currentExitingVertexes) => newCumulatedValue
    *
    * where:
    *
    * <ul>
    * <li><b>cumulatedValue</b> is the value cumulated until now</li>
    * <li><b>currentEnteringArcs</b> is the set of arcs entering the current vertex</li>
    * <li><b>currentVertex</b> is the vertex now explored by fold()</li>
    * <li><b>currentExitingArcs</b> is the set of arcs exiting the current vertex</li>
    * <li><b>currentExitingVertexes</b> is the set of vertexes that are target of an arc exiting the current vertex</li>
    * </ul>
    *
    * The function must return <b>newCumulatedValue</b>, used by fold() as the return value or to call the next VertexFoldProcessor
    *
    * @tparam T The type of the cumulated value
    */
  type VertexFoldProcessor[T] = (T, Set[L], V, Set[L], Set[V]) => T


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
     * At the beginning of the algorithm, no vertex has been expanded and no link has been explored;
     * consequently, the fringe coincides with the root vertexes - the ones having no entering arcs.
     */
    fold(
      initialValue,
      vertexFoldProcessor,
      Set(),
      Set(),
      rootVertexes.toList
    )
  }


  @tailrec
  private final def fold[T](
                             cumulatedValue: T,
                             vertexFoldProcessor: VertexFoldProcessor[T],
                             processedVertexes: Set[V],
                             exploredArcs: Set[L],
                             fringe: List[V]
                                          ): T = {
    fringe match {
      /*
       * In this first case, the fringe contains at least one vertex to consider.
       */
      case currentVertex :: fringeTail =>
        val currentEnteringArcs =
          enteringArcsMap.getOrElse(currentVertex, Set())

        /*
         * If all the arcs entering the current vertex have already been explored,
         * such vertex conceptually behaves now like a root vertex and can be processed.
         */
        if (currentEnteringArcs.subsetOf(exploredArcs)) {
          val currentExitingArcs =
            exitingArcsMap.getOrElse(currentVertex, Set())

          val currentExitingVertexes =
            exitingVertexesMap.getOrElse(currentVertex, Set())

          val newCumulatedValue =
            vertexFoldProcessor(
              cumulatedValue,

              currentEnteringArcs,

              currentVertex,

              currentExitingArcs,

              currentExitingVertexes
            )

          /**
           * The next step can occur, notifying that the current vertex has been processed
           * and that its explored arcs have been explored; in particular, the fringe can now
           * include all of its exiting vertexes.
           */
          fold(
            newCumulatedValue,

            vertexFoldProcessor,

            processedVertexes + currentVertex,

            exploredArcs ++ currentExitingArcs,

            (fringeTail ++ currentExitingVertexes).distinct
          )
        } else {
          /*
           * If the current vertex has at least an entering link that was not already explored,
           * it must be removed from the fringe: it will be re-added later, after processing
           * one of its entering vertexes.
           */
          fold(
            cumulatedValue,

            vertexFoldProcessor,

            processedVertexes,

            exploredArcs,

            fringeTail
          )
        }

      /*
       * When the fringe is empty, there is no more vertex that can be expanded.
       */
      case Nil =>
        /*
         * If the expanded vertexes are precisely all the vertexes, the algorithm has succeeded;
         * otherwise, it means that we are stuck in a cycle.
         */
        if (processedVertexes.size == vertexes.size)
          cumulatedValue
        else
          throw new CircularGraphException
    }
  }
}
