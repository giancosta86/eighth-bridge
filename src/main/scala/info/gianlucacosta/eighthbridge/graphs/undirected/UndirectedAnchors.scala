package info.gianlucacosta.eighthbridge.graphs.undirected

import info.gianlucacosta.eighthbridge.graphs.Vertex

import scala.language.implicitConversions

case class UndirectedAnchors[V <: Vertex](oneVertex: V, anotherVertex: V) {
  def update(updatedVertex: V): UndirectedAnchors[V] =
    copy(
      oneVertex =
        oneVertex.update(updatedVertex),

      anotherVertex =
        anotherVertex.update(updatedVertex)
    )

  override def equals(obj: Any): Boolean =
    obj match {
      case other: UndirectedAnchors[V] =>
        (oneVertex == other.oneVertex && anotherVertex == other.anotherVertex) ||
          (oneVertex == other.anotherVertex && anotherVertex == other.oneVertex)

      case _ =>
        false
    }

  override def hashCode(): Int =
    oneVertex.hashCode() + anotherVertex.hashCode()
}

object UndirectedAnchors {
  implicit def toPair[V <: Vertex](self: UndirectedAnchors[V]): (V, V) =
    (self.oneVertex, self.anotherVertex)

  implicit def toSet[V <: Vertex](self: UndirectedAnchors[V]): Set[V] =
    Set(self.oneVertex, self.anotherVertex)
}