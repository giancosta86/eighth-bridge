package info.gianlucacosta.eighthbridge.graphs.point2point

import info.gianlucacosta.eighthbridge.graphs.{Link, Vertex}

import scala.annotation.tailrec

/**
  * Directed graph overriding topological functions such as getEnteringVertexes() and fold() so that
  * they employ an internal cache which is lazily initialized - leading to far better performances.
  *
  * @tparam V Vertex type
  * @tparam L Link type
  */
trait TopologyCacheDirectedGraph[V <: Vertex, L <: Link]
  extends DirectedGraph[V, L] {
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
  private lazy val arcsBetweenMap: Map[(V, V), Set[L]] =
    topologyCache
      .map { case (sourceVertex, arc, targetVertex) =>
        (sourceVertex -> targetVertex) -> arc
      }
      .groupBy(_._1)
      .mapValues(_.map(_._2))


  override def getEnteringVertexes(vertex: V): Set[V] =
    enteringVertexesMap.getOrElse(
      vertex,
      Set()
    )


  override def getExitingVertexes(vertex: V): Set[V] =
    exitingVertexesMap.getOrElse(
      vertex,
      Set()
    )


  override def getEnteringArcs(vertex: V): Set[L] =
    enteringArcsMap.getOrElse(
      vertex,
      Set()
    )


  override def getExitingArcs(vertex: V): Set[L] =
    exitingArcsMap.getOrElse(
      vertex,
      Set()
    )


  override def getArcsBetween(sourceVertex: V, targetVertex: V): Set[L] =
    arcsBetweenMap.getOrElse(
      sourceVertex -> targetVertex,
      Set()
    )


  override def getLinksBetween(linkVertexes: Set[V]): Set[L] = {
    require(linkVertexes.size == 2)

    val firstVertex =
      linkVertexes.head

    val secondVertex =
      linkVertexes.last

    val firstToSecondArcs =
      arcsBetweenMap.getOrElse(
        firstVertex -> secondVertex,
        Set()
      )

    val secondToFirstArcs =
      arcsBetweenMap.getOrElse(
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


  override def fold[T](initialValue: T)(vertexProcessor: VertexFoldProcessor[T]): T =
    fold(
      initialValue,
      vertexProcessor,
      enteringArcsMap,
      exitingArcsMap,
      exitingVertexesMap,
      Set(),
      Set(),
      rootVertexes.toList
    )
}
