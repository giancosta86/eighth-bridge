package info.gianlucacosta.eighthbridge.theory

import info.gianlucacosta.eighthbridge.theory.entities.{Entity, IntBasedEntity}

trait Vertex extends IntBasedEntity


object Vertex {
  implicit class VertexSetExtensions[V <: Vertex](self: Set[V]) {
    def containsPair(vertexPair: (V, V)): Boolean =
      self.contains(vertexPair._1) && (self.contains(vertexPair._2))

    def intersectsPair(vertexPair: (V, V)): Boolean =
      self.contains(vertexPair._1) || self.contains(vertexPair._2)
  }
}