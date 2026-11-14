package info.gianlucacosta.eighthbridge.graphs

abstract class Anchors[V <: Vertex](first: V, second: V) {
  def toSet: Set[V] =
    Set(first, second)

  def toPair: (V, V) =
    (first, second)

  def update(updatedVertex: V): this.type

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
