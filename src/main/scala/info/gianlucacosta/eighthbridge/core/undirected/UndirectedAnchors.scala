package info.gianlucacosta.eighthbridge.core.undirected

import info.gianlucacosta.eighthbridge.core.{Anchors, Vertex}

class UndirectedAnchors[V <: Vertex](oneVertex: V, anotherVertex: V)
  extends Anchors[V](oneVertex, anotherVertex) {

  override def update(updatedVertex: V): this.type =
    new UndirectedAnchors[V](
      oneVertex =
        oneVertex.update(updatedVertex),

      anotherVertex =
        anotherVertex.update(updatedVertex)
    )
      .asInstanceOf[this.type]

  override protected def actualEquals(other: Anchors[V]): Boolean =
    toSet == other.toSet

  override def toString: String =
    s"{${oneVertex}, ${anotherVertex}}"
}