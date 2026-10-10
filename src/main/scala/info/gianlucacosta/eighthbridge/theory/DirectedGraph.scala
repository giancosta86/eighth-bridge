package info.gianlucacosta.eighthbridge.theory

import scala.annotation.tailrec

trait DirectedGraph[V <: Vertex, L <: Link] {
  val vertexes: Set[V]

  protected val arcsByVertexPair: Map[(V, V), Set[L]]

  protected def graphCopy(
                           vertexes: Set[V] = vertexes,
                           arcsByVertexPair: Map[(V, V), Set[L]] = arcsByVertexPair
   ): this.type


  def addVertexes(vertexesToAdd: Set[V]): this.type =
    graphCopy(
      vertexes = vertexes ++ vertexesToAdd
    )

  final def addVertexes(vertexesToAdd: V*): this.type =
    addVertexes(vertexesToAdd.toSet)

  final def addVertex(vertex: V): this.type =
    graphCopy(
      vertexes = vertexes + vertex
    )

  def updateVertex(updatedVertex: V): this.type =
    graphCopy(
      vertexes =
        vertexes.updateVertex(updatedVertex),

      arcsByVertexPair =
        arcsByVertexPair.map {
          case (keyPair, link) =>
            keyPair.updateVertex(updatedVertex) -> link
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

      arcsByVertexPair = arcsByVertexPair.filterKeys(
        !vertexesToRemove.intersectsPair(_)
      )
    )

  final def removeVertexes(vertexes: V*): this.type =
    removeVertexes(vertexes.toSet)

  final def removeVertex(vertex: V): this.type =
    removeVertexes(Set(vertex))

  def addArc(sourceVertex: V, targetVertex: V, arc: L): this.type = {
    val vertexPair =
      sourceVertex -> targetVertex

    if (vertexes.containsPair(vertexPair)) {
      val updatedLinks =
        arcsByVertexPair.getOrElse(vertexPair, Set()) + arc

      graphCopy(
        arcsByVertexPair =
          arcsByVertexPair + (vertexPair -> updatedLinks)
      )
    } else
      this
  }

  final def addArc(vertexPair: (V, V), link: L): this.type =
    addArc(vertexPair._1, vertexPair._2, link)

  def updateLink(updatedLink: L): this.type =
    graphCopy(
      arcsByVertexPair =
        arcsByVertexPair.mapValues(_.updateLink(updatedLink))
    )

  final def updateLinks(updatedLinks: Set[L]): this.type =
    updatedLinks.foldLeft(this)((cumulatedGraph, link) =>
      cumulatedGraph.updateLink(link)
    ).asInstanceOf[this.type]

  final def updateLinks(updatedLinks: L*): this.type =
    updateLinks(updatedLinks.toSet)

  def removeLinks(linksToRemove: Set[L]): this.type =
    graphCopy(
      arcsByVertexPair =
        arcsByVertexPair
          .mapValues(
          _ -- linksToRemove
        )
          .filter {
          case (_, linkSet) =>
            linkSet.nonEmpty
        }
    )

  final def removeLinks(links: L*): this.type =
    removeLinks(links.toSet)

  final def removeLink(link: L): this.type =
    removeLinks(Set(link))

  @transient
  lazy val links: Set[L] =
    arcsByVertexPair
      .values
      .flatten
      .toSet

  @transient
  protected lazy val exitingVertexesByVertex: Map[V, Set[V]] =
    arcsByVertexPair
      .keySet
      .groupBy {
        case (sourceVertex, _) => sourceVertex
      }
      .mapValues(
        _.map { case (_, targetVertex ) =>
          targetVertex
        })

  @transient
  protected lazy val exitingArcsByVertex: Map[V, Set[L]] =
    arcsByVertexPair
      .flattenByVertex { case (sourceVertex, _) =>
        sourceVertex
      }

  @transient
  protected lazy val enteringVertexesByVertex: Map[V, Set[V]] =
    arcsByVertexPair
      .keySet
      .groupBy {
        case (_, targetVertex) => targetVertex
      }
      .mapValues(_.map { case (sourceVertex, _ ) => sourceVertex })

  @transient
  protected lazy val enteringArcsByVertex: Map[V, Set[L]] =
    arcsByVertexPair
      .flattenByVertex { case (_, targetVertex) => targetVertex }

  @transient
  protected lazy val vertexPairsByArc: Map[L, (V, V)] =
    arcsByVertexPair
      .flatMap { case (vertexPair, links) =>
        links.map(_ -> vertexPair)
      }

  /**
   * Returns the set of vertexes that are target of any link exiting the given vertex.
   */
  def getExitingVertexes(vertex: V): Set[V] =
    exitingVertexesByVertex.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose source is the given vertex.
   */
  def getExitingArcs(vertex: V): Set[L] =
    exitingArcsByVertex.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of vertexes that are source of any link entering the given vertex.
   */
  def getEnteringVertexes(vertex: V): Set[V] =
    enteringVertexesByVertex.getOrElse(
      vertex,
      Set()
    )

  /**
   * Returns the set of arcs whose target is the given vertex.
   */
  def getEnteringArcs(vertex: V): Set[L] =
    enteringArcsByVertex.getOrElse(
      vertex,
      Set()
    )

  def getArcsBetween(vertexPair: (V, V)): Set[L] =
    arcsByVertexPair.getOrElse(
      vertexPair,
      Set()
    )

  final def getArcsBetween(sourceVertex: V, targetVertex: V): Set[L] =
    getArcsBetween(sourceVertex -> targetVertex)

  def getEdgesBetween(oneVertex: V, anotherVertex: V): Set[L] = {
    getArcsBetween(oneVertex -> anotherVertex) ++
      getArcsBetween(anotherVertex -> oneVertex)
  }

  def getLinkedVertexes(vertex: V): Set[V] =
    getExitingVertexes(vertex) ++ getEnteringVertexes(vertex)

  @transient
  lazy val linkedVertexes: Set[V] =
    exitingArcsByVertex.keySet ++ enteringArcsByVertex.keySet

  @transient
  lazy val unlinkedVertexes: Set[V] =
    vertexes -- linkedVertexes

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

  def getVertexPair(arc: L): Option[(V, V)] =
    vertexPairsByArc.get(arc)


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
          enteringVertexesByVertex.getOrElse(currentVertex, Set())

        /*
         * If all the vertexes pointing to the current vertex have already been explored,
         * such vertex conceptually behaves now like a root vertex and can be processed.
         */
        if (currentEnteringVertexes.subsetOf(processedVertexes)) {
          val currentExitingVertexes =
            exitingVertexesByVertex.getOrElse(currentVertex, Set())

          val vertexTopology = VertexTopology(
            enteringVertexes = currentEnteringVertexes,
            enteringArcs =
              enteringArcsByVertex.getOrElse(currentVertex, Set()),
            exitingArcs =
              exitingArcsByVertex.getOrElse(currentVertex, Set()),
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