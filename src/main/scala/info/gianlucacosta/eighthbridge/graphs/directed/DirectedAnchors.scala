package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.{Anchors, Vertex}

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

  override protected def checkEquality(other: Anchors[V]): Boolean =
    toPair == other.toPair
}
