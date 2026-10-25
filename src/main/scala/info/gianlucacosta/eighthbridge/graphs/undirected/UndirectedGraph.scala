package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.directed.{DirectedGraph, VertexFoldProcessor, VertexTopology}
import info.gianlucacosta.eighthbridge.graphs.{CircularGraphException, GraphCore, Link, Vertex}

import scala.annotation.tailrec

final class UndirectedGraph[V <: Vertex, E <: Link] (
                                                              vertexes: Set[V],
                                                              private val edgesByAnchorSet: Map[Set[V], Set[E]]
                                                            )
  extends GraphCore[V](vertexes) {

  def this() = this(
    vertexes =
      Set[V](),

    edgesByAnchorSet =
      Map[Set[V], Set[E]]()
  )

  //TODO! Strictly speaking, this could merely return "UndirectedGraph" in lieu of "this.type" - which applies to the methods calling it, as well.
  private def copy(
                    vertexes: Set[V] = vertexes,
                    edgesByVertexSet: Map[Set[V], Set[E]] = edgesByAnchorSet
                  ): this.type =
    new UndirectedGraph(
      vertexes =
        vertexes,

      edgesByAnchorSet =
        edgesByVertexSet
    ).asInstanceOf[this.type]

  override protected def setVertexes(updatedVertexes: Set[V]): this.type =
    copy(
      vertexes =
        updatedVertexes
    )

  def updateVertex(updatedVertex: V): this.type =
    copy(
      vertexes =
        vertexes.updateEntity(updatedVertex),

      edgesByVertexSet =
        edgesByAnchorSet.map {
          case (vertexSet, edge) =>
            vertexSet.updateEntity(updatedVertex) -> edge
        }
    )

  def removeVertexes(vertexesToRemove: Set[V]): this.type =
    copy(
      vertexes =
        vertexes -- vertexesToRemove,

      edgesByVertexSet =
        edgesByAnchorSet.filterKeys(anchorSet =>
          (vertexesToRemove & anchorSet).isEmpty
        )
    )

  def addEdge(anchorSet: Set[V], edge: E): this.type = {
    if (anchorSet.subsetOf(vertexes)) {
      val updatedEdgeSet =
        edgesByAnchorSet.getOrElse(anchorSet, Set()) + edge

      copy(
        edgesByVertexSet =
          edgesByAnchorSet + (anchorSet -> updatedEdgeSet)
      )
    } else
      this
  }

  def addEdge(oneVertex: V, anotherVertex: V, edge: E): this.type = {
    val anchorSet: Set[V] =
      Set(oneVertex, anotherVertex)

    addEdge(anchorSet, edge)
  }

  def updateEdge(updatedEdge: E): this.type =
    copy(
      edgesByVertexSet =
        edgesByAnchorSet.mapValues(_.updateEntity(updatedEdge))
    )

  def updateEdges(updatedEdges: Set[E]): this.type =
    updatedEdges.foldLeft(this)((cumulatedGraph, edge) =>
      cumulatedGraph.updateEdge(edge)
    ).asInstanceOf[this.type]

  def updateEdges(updatedEdges: E*): this.type =
    updateEdges(updatedEdges.toSet)

  def removeEdges(edgesToRemove: Set[E]): this.type =
    copy(
      edgesByVertexSet =
        edgesByAnchorSet
          .mapValues(
            _ -- edgesToRemove
          )
          .filter { case (_, edgeSet) =>
            edgeSet.nonEmpty
          }
    )

  def removeEdges(edges: E*): this.type =
    removeEdges(edges.toSet)

  def removeEdge(edge: E): this.type =
    removeEdges(Set(edge))

  @transient
  lazy val edges: Set[E] =
    edgesByAnchorSet
      .values
      .flatten
      .toSet


  @transient
  protected lazy val edgesById: Map[Link#EntityId, E] =
    edges
      .map(edge => edge.id -> edge)
      .toMap

  def getEdgeById(id: Link#EntityId): Option[E] =
    edgesById.get(id)

  def getEdgesBetween(anchorSet: Set[V]): Set[E] =
    edgesByAnchorSet.get(anchorSet).getOrElse(Set[E]())

  def getEdgesBetween(oneVertex: V, anotherVertex: V): Set[E] =
    getEdgesBetween(Set(oneVertex, anotherVertex))


  @transient
  protected lazy val anchorSetByEdge: Map[E, Set[V]] =
    edgesByAnchorSet
      .flatMap { case (anchorSet, edges) =>
        edges.map(_ -> anchorSet)
      }

  def getAnchorSet(edge: E): Option[Set[V]] =
    anchorSetByEdge.get(edge)



  @transient
  private lazy val connectedEdgesByVertex: Map[V, Set[E]] =
    edgesByAnchorSet
      .toList
      .flatMap { case (vertexes, edges) =>
        vertexes.map(_ -> edges)
      }
      .groupBy(_._1)
      .mapValues(
        _.flatMap(_._2)
          .toSet
      )

  def getConnectedEdges(vertex: V): Set[E] =
    connectedEdgesByVertex.getOrElse(
      vertex,
      Set()
    )

  @transient
  private lazy val neighborsByVertex: Map[V, Set[V]] =
    edgesByAnchorSet
      .keySet.flatMap(anchorSet =>
        Seq(
          anchorSet.head -> anchorSet.last,
          anchorSet.last -> anchorSet.head
        )
      )
      .groupBy(_._1)
      .mapValues(_.map(_._2).toSet)

  def getNeighbors(vertex: V): Set[V] =
    neighborsByVertex.getOrElse(vertex, Set())

  @transient
  lazy val connectedVertexes: Set[V] =
    connectedEdgesByVertex.keySet

  @transient
  lazy val isolatedVertexes: Set[V] =
    vertexes -- connectedVertexes

  @transient
  lazy val isCyclic: Boolean = ???
}
