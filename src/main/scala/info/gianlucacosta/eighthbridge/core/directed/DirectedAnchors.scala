package info.gianlucacosta.eighthbridge.core.directed

import info.gianlucacosta.eighthbridge.core.{Anchors, Vertex}

class DirectedAnchors[V <: Vertex](val source: V, val target: V)
  extends Anchors[V](source, target) {

  override def update(updatedVertex: V): this.type =
    new DirectedAnchors[V](
      source =
        source.update(updatedVertex),
      target =
        target.update(updatedVertex)
    )
      .asInstanceOf[this.type]

  override protected def actualEquals(other: Anchors[V]): Boolean =
    toPair == other.toPair

  override def toString: String =
    s"(${source}, ${target})"
}
