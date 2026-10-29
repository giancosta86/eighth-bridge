package info.gianlucacosta.eighthbridge.graphs

abstract class GraphCore[V <: Vertex, L <: Link](val vertexes: Set[V])
  extends Graph[V, L] {
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
}
