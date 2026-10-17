package info.gianlucacosta.eighthbridge.theory

import info.gianlucacosta.eighthbridge.theory.entities.Entity

trait Vertex extends Entity


object Vertex {
  implicit class VertexExtensions[V <: Vertex](self: V) {
    def update(updatedVertex: V): V =
      if (self == updatedVertex)
        updatedVertex
      else
        self
  }

  implicit class VertexSetExtensions[V <: Vertex](self: Set[V]) {
    def updateVertex(updatedVertex: V): Set[V] =
      self.map(_.update(updatedVertex))

    def containsPair(vertexPair: (V, V)): Boolean =
      self.contains(vertexPair._1) && (self.contains(vertexPair._2))

    def intersectsPair(vertexPair: (V, V)): Boolean =
      self.contains(vertexPair._1) || self.contains(vertexPair._2)
  }

  implicit class VertexPairExtensions[V <: Vertex](self: (V, V)) {
    def updateVertex(updatedVertex: V): (V, V) =
      (
        self._1.update(updatedVertex),
        self._2.update(updatedVertex)
      )
  }
}