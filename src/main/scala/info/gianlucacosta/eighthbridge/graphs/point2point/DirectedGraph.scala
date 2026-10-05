package info.gianlucacosta.eighthbridge.graphs.point2point

import info.gianlucacosta.eighthbridge.graphs.{Graph, Link, Vertex}

import java.util.UUID
import scala.annotation.tailrec

/**
  * A directed graph - that is, a graph whose Binding type parameter resolves to ArcBinding
  *
  * @tparam V Vertex type
  * @tparam L Link type
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
          cumulatedCache
            + cacheEntry,

          tailBindings
        )

      case Nil =>
        cumulatedCache
    }
  }


  private def createTopologyCacheEntry(binding: ArcBinding): (V, L, V) = {
    val sourceVertex =
      getVertex(binding.sourceVertexId).get

    val arc =
      getLink(binding.linkId).get

    val targetVertex =
      getVertex(binding.targetVertexId).get

    (sourceVertex, arc, targetVertex)
  }

  @transient
  private lazy val exitingVertexesMap: Map[V, Set[V]] =
    topologyCache
      .map { case (sourceVertex, _, targetVertex) =>
        sourceVertex -> targetVertex
      }
      .groupBy(_._1)
      .mapValues(_.map(_._2))


  @transient
  private lazy val exitingArcsMap: Map[V, Set[L]] =
    topologyCache
      .map { case (sourceVertex, arc, _) =>
        sourceVertex -> arc
      }
      .groupBy(_._1)
      .mapValues(_.map(_._2))


  @transient
  private lazy val enteringVertexesMap: Map[V, Set[V]] =
    topologyCache
      .map { case (sourceVertex, _, targetVertex) =>
        targetVertex -> sourceVertex
      }
      .groupBy(_._1)
      .mapValues(_.map(_._2))


  @transient
  private lazy val enteringArcsMap: Map[V, Set[L]] =
    topologyCache
      .map { case (_, arc, targetVertex) =>
        targetVertex -> arc
      }
      .groupBy(_._1)
      .mapValues(_.map(_._2))


  @transient
  private lazy val arcsBetweenPairsMap: Map[(V, V), Set[L]] =
    topologyCache
      .map { case (sourceVertex, arc, targetVertex) =>
        (sourceVertex -> targetVertex) -> arc
      }
      .groupBy(_._1)
      .mapValues(_.map(_._2))

  /**
   * The vertexes having no entering arcs
   */
  @transient
  lazy val rootVertexes: Set[V] =
    vertexes
      .filter(getEnteringArcs(_).isEmpty)


  /**
   * Returns the set of vertexes that are target of any arc exiting the given vertex
   */
  def getExitingVertexes(vertex: V): Set[V] =
    exitingVertexesMap.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose source is the given vertex
   */
  def getExitingArcs(vertex: V): Set[L] =
    exitingArcsMap.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of vertexes that are source of any arc entering the given vertex
   */
  def getEnteringVertexes(vertex: V): Set[V] =
    enteringVertexesMap.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose target is the given vertex
   */
  def getEnteringArcs(vertex: V): Set[L] =
    enteringArcsMap.getOrElse(
      vertex,
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
    arcsBetweenPairsMap.getOrElse(
      sourceVertex -> targetVertex,
      Set()
    )

  def getArcsBetween(vertexPair: (V, V)): Set[L] =
    getArcsBetween(vertexPair._1, vertexPair._2)

  override def getLinksBetween(linkVertexes: Set[V]): Set[L] = {
    require(linkVertexes.size == 2)

    val firstVertex = linkVertexes.head

    val secondVertex = linkVertexes.last

    val firstToSecondArcs =
      arcsBetweenPairsMap.getOrElse(
        firstVertex -> secondVertex,
        Set()
      )

    val secondToFirstArcs =
      arcsBetweenPairsMap.getOrElse(
        secondVertex -> firstVertex,
        Set()
      )

    firstToSecondArcs ++
      secondToFirstArcs
  }

  override def getLinksBetween(linkVertexes: V*): Set[L] =
    getLinksBetween(linkVertexes.toSet)

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

    enteringVertexes ++
      exitingVertexes
  }

  /**
    * Function passed to fold(). Its signature must be:
    * (cumulatedValue, currentEnteringArcs, currentVertex, currentExitingArcs) => newCumulatedValue
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
  type VertexFoldProcessor[T] =
  (T, Set[L], V, Set[L], Set[V]) => T


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
                                            expandedVertexes: Set[V],
                                            exploredArcs: Set[L],
                                            fringe: List[V]
                                          ): T = {
    fringe match {
      case currentVertex :: fringeTail =>
        val currentEnteringArcs =
          enteringArcsMap.getOrElse(currentVertex, Set())


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

          fold(
            newCumulatedValue,

            vertexFoldProcessor,

            expandedVertexes +
              currentVertex,

            exploredArcs ++
              currentExitingArcs,

            (fringeTail ++
              currentExitingVertexes).distinct
          )
        } else {
          fold(
            cumulatedValue,

            vertexFoldProcessor,

            expandedVertexes,

            exploredArcs,

            fringeTail
          )
        }


      case Nil =>
        if (expandedVertexes.size == vertexes.size)
          cumulatedValue
        else
          throw new CircularGraphException
    }
  }
}
