package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.directed.{DirectedGraph, VertexFoldProcessor, VertexTopology}
import info.gianlucacosta.eighthbridge.graphs.{CircularGraphException, Graph, Link, Vertex}

import scala.annotation.tailrec

final class UndirectedGraph[V <: Vertex, E <: Link] (
                                                              vertexes: Set[V],
                                                              private val edgesByAnchorSet: Map[Set[V], Set[E]]
                                                            )
  extends Graph[V, E](vertexes) {

  def this() = this(
    vertexes =
      Set[V](),

    edgesByAnchorSet =
      Map[Set[V], Set[E]]()
  )

  override protected def setVertexes(updatedVertexes: Set[V]): this.type =
    new UndirectedGraph[V, E](
      vertexes =
        updatedVertexes,
      edgesByAnchorSet =
        edgesByAnchorSet
    )
      .asInstanceOf[this.type]

  def updateVertex(updatedVertex: V): this.type =
    new UndirectedGraph[V, E](
      vertexes =
        vertexes.updateEntity(updatedVertex),

      edgesByAnchorSet =
        edgesByAnchorSet.map {
          case (vertexSet, edge) =>
            vertexSet.updateEntity(updatedVertex) -> edge
        }
    )
      .asInstanceOf[this.type]

  def removeVertexes(vertexesToRemove: Set[V]): this.type =
    new UndirectedGraph[V, E](
      vertexes =
        vertexes -- vertexesToRemove,

      edgesByAnchorSet =
        edgesByAnchorSet.filterKeys(anchorSet =>
          (vertexesToRemove & anchorSet).isEmpty
        )
    )
      .asInstanceOf[this.type]

  def addEdge(anchorSet: Set[V], edge: E): UndirectedGraph[V, E] = {
    if (anchorSet.subsetOf(vertexes)) {
      val updatedEdgeSet =
        edgesByAnchorSet.getOrElse(anchorSet, Set()) + edge

      new UndirectedGraph[V, E](
        vertexes =
          vertexes,
        edgesByAnchorSet =
          edgesByAnchorSet + (anchorSet -> updatedEdgeSet)
      )
    } else
      this
  }

  def addEdge(oneVertex: V, anotherVertex: V, edge: E): UndirectedGraph[V, E] = {
    val anchorSet: Set[V] =
      Set(oneVertex, anotherVertex)

    addEdge(anchorSet, edge)
  }

  def updateEdge(updatedEdge: E): UndirectedGraph[V, E] =
    new UndirectedGraph[V, E](
      vertexes =
        vertexes,
      edgesByAnchorSet =
        edgesByAnchorSet.mapValues(_.updateEntity(updatedEdge))
    )

  def updateEdges(updatedEdges: Set[E]): UndirectedGraph[V, E] =
    updatedEdges.foldLeft(this)((cumulatedGraph, edge) =>
      cumulatedGraph.updateEdge(edge)
    )

  def updateEdges(updatedEdges: E*): UndirectedGraph[V, E] =
    updateEdges(updatedEdges.toSet)

  def removeEdges(edgesToRemove: Set[E]): UndirectedGraph[V, E] =
    new UndirectedGraph[V, E](
      vertexes =
        vertexes,
      edgesByAnchorSet =
        edgesByAnchorSet
          .mapValues(
            _ -- edgesToRemove
          )
          .filter { case (_, edgeSet) =>
            edgeSet.nonEmpty
          }
    )

  def removeEdges(edges: E*): UndirectedGraph[V, E] =
    removeEdges(edges.toSet)

  def removeEdge(edge: E): UndirectedGraph[V, E] =
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

  override def links: Set[E] =
    edges

  override def getLinkById(id: Int): Option[E] =
    getEdgeById(id)
}
