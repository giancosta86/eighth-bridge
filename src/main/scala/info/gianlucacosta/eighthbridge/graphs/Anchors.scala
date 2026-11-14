package info.gianlucacosta.eighthbridge.graphs

abstract class Anchors[V <: Vertex](first: V, second: V) {
  def toSet: Set[V] =
    Set(first, second)

  def toPair: (V, V) =
    (first, second)

  def update(updatedVertex: V): this.type

  protected def checkEquality(other: Anchors[V]): Boolean

  override def equals(obj: Any): Boolean =
    obj match {
      case other: Anchors[V] =>
        checkEquality(other)

      case _ =>
        false
    }

  override def hashCode(): Int =
    toSet.hashCode()
}
