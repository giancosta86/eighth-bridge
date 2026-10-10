package info.gianlucacosta.eighthbridge.theory

trait Vertex extends Entity


object Vertex {
  implicit class VertexExtensions[V <: Vertex](vertex: V) {
    def update(updatedVertex: V): V =
      if (vertex == updatedVertex)
        updatedVertex
      else
        vertex
  }

  implicit class VertexSetExtensions[V <: Vertex](vertexSet: Set[V]) {
    def updateVertex(updatedVertex: V): Set[V] =
      vertexSet.map(_.update(updatedVertex))

    def containsPair(vertexPair: (V, V)): Boolean =
      vertexSet.contains(vertexPair._1) && (vertexSet.contains(vertexPair._2))

    def intersectsPair(vertexPair: (V, V)): Boolean =
      vertexSet.contains(vertexPair._1) || vertexSet.contains(vertexPair._2)
  }

  implicit class VertexPairExtensions[V <: Vertex](vertexPair: (V, V)) {
    def updateVertex(updatedVertex: V): (V, V) =
      (
        vertexPair._1.update(updatedVertex),
        vertexPair._2.update(updatedVertex)
      )
  }
}