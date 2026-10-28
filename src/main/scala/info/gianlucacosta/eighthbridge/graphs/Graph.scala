package info.gianlucacosta.eighthbridge.graphs

import info.gianlucacosta.eighthbridge.graphs.entities.IntBasedEntity

abstract class Graph[V <: Vertex, L <: Link](val vertexes: Set[V]) {
  override def equals(obj: Any): Boolean =
    false

  override def hashCode(): Int =
    0

  def isEmpty: Boolean =
    vertexes.isEmpty

  @transient
  def nonEmpty: Boolean =
    vertexes.nonEmpty

  protected def setVertexes(updatedVertexes: Set[V]): this.type

  def addVertexes(vertexesToAdd: Set[V]): this.type =
    setVertexes(
      vertexes ++ vertexesToAdd
    )

  final def addVertexes(vertexesToAdd: V*): this.type =
    addVertexes(vertexesToAdd.toSet)

  final def addVertex(vertex: V): this.type =
    setVertexes(
      vertexes + vertex
    )

  def updateVertex(updatedVertex: V): this.type

  final def updateVertexes(updatedVertexes: Set[V]): this.type =
    updatedVertexes.foldLeft(this)((cumulatedGraph, vertex) =>
      cumulatedGraph.updateVertex(vertex)
    ).asInstanceOf[this.type]

  final def updateVertexes(updatedVertexes: V*): this.type =
    updateVertexes(updatedVertexes.toSet)

  def removeVertexes(vertexesToRemove: Set[V]): this.type

  final def removeVertexes(vertexes: V*): this.type =
    removeVertexes(vertexes.toSet)

  final def removeVertex(vertex: V): this.type =
    removeVertexes(Set(vertex))

  @transient
  protected lazy val vertexesById: Map[Vertex#EntityId, V] =
    vertexes
      .map(vertex => vertex.id -> vertex)
      .toMap

  def getVertexById(id: Vertex#EntityId): Option[V] =
    vertexesById.get(id)

  def links: Set[L]

  def getLinkById(id: Link#EntityId): Option[L]
}
