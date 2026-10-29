package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.directed.{DirectedGraph, VertexFoldProcessor, VertexTopology}
import info.gianlucacosta.eighthbridge.graphs.{CircularGraphException, GraphCore, Link, Vertex}

import scala.annotation.tailrec

final class UndirectedGraph[V <: Vertex, E <: Link] private (
                                                              vertexes: Set[V],
                                                              private val edgesByAnchors: Map[UndirectedAnchors[V], Set[E]]
                                                            )
  extends GraphCore[V, E](vertexes) {

  def this() = this(
    vertexes =
      Set[V](),

    edgesByAnchors =
      Map[UndirectedAnchors[V], Set[E]]()
  )

  override protected def setVertexes(updatedVertexes: Set[V]): this.type =
    new UndirectedGraph[V, E](
      vertexes =
        updatedVertexes,
      edgesByAnchors =
        edgesByAnchors
    )
      .asInstanceOf[this.type]

  def updateVertex(updatedVertex: V): this.type =
    new UndirectedGraph[V, E](
      vertexes =
        vertexes.update(updatedVertex),

      edgesByAnchors =
        edgesByAnchors.map {
          case (anchors, edge) =>
            anchors.update(updatedVertex) -> edge
        }
    )
      .asInstanceOf[this.type]

  def removeVertexes(vertexesToRemove: Set[V]): this.type =
    new UndirectedGraph[V, E](
      vertexes =
        vertexes -- vertexesToRemove,

      edgesByAnchors =
        edgesByAnchors.filterKeys(anchors =>
          (vertexesToRemove & anchors).isEmpty
        )
    )
      .asInstanceOf[this.type]

  def addEdge(anchors: UndirectedAnchors[V], edge: E): UndirectedGraph[V, E] = {
    if (anchors.toSet.subsetOf(vertexes)) {
      val updatedEdgeSet =
        edgesByAnchors.getOrElse(anchors, Set()) + edge

      new UndirectedGraph[V, E](
        vertexes =
          vertexes,
        edgesByAnchors =
          edgesByAnchors + (anchors -> updatedEdgeSet)
      )
    } else
      this
  }

  def addEdge(oneVertex: V, anotherVertex: V, edge: E): UndirectedGraph[V, E] = {
    val anchors =
      UndirectedAnchors(oneVertex, anotherVertex)

    addEdge(anchors, edge)
  }

  def updateEdge(updatedEdge: E): UndirectedGraph[V, E] =
    new UndirectedGraph[V, E](
      vertexes =
        vertexes,
      edgesByAnchors =
        edgesByAnchors.mapValues(_.update(updatedEdge))
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
      edgesByAnchors =
        edgesByAnchors
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
    edgesByAnchors
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

  def getEdgesBetween(anchors: UndirectedAnchors[V]): Set[E] =
    edgesByAnchors.getOrElse(anchors, Set[E]())

  def getEdgesBetween(oneVertex: V, anotherVertex: V): Set[E] =
    getEdgesBetween(UndirectedAnchors(oneVertex, anotherVertex))


  @transient
  protected lazy val anchorSetByEdge: Map[E, UndirectedAnchors[V]] =
    edgesByAnchors
      .flatMap { case (anchorSet, edges) =>
        edges.map(_ -> anchorSet)
      }

  def getAnchorSet(edge: E): Option[UndirectedAnchors[V]] =
    anchorSetByEdge.get(edge)

  @transient
  private lazy val connectedEdgesByVertex: Map[V, Set[E]] =
    edgesByAnchors
      .toList
      .flatMap { case (anchors, edges) =>
        anchors.toSet[V].map(vertex =>
          vertex -> edges
        )
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
    edgesByAnchors
      .keySet.flatMap(anchors =>
        Seq(
          anchors.oneVertex -> anchors.anotherVertex,
          anchors.anotherVertex -> anchors.oneVertex
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
