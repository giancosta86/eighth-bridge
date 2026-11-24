package info.gianlucacosta.eighthbridge.core

abstract class Anchors[V <: Vertex](val first: V, val second: V) {
  def toSet: Set[V] =
    Set(first, second)

  def toPair: (V, V) =
    (first, second)

  def update(updatedVertex: V): this.type

  def getPairedWith(vertex: V): Option[V] = {
    if (vertex == first)
      Some(second)
    else if (vertex == second)
      Some(first)
    else
      None
  }

  override def equals(obj: Any): Boolean =
    obj match {
      case other: Anchors[V] =>
        actualEquals(other)

      case _ =>
        false
    }

  protected def actualEquals(other: Anchors[V]): Boolean

  override def hashCode(): Int =
    toSet.hashCode()
}