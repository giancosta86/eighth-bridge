package info.gianlucacosta.eighthbridge.graphs

trait Graph[V <: Vertex, L <: Link] {
  val vertexes: Set[V]

  @transient
  protected lazy val vertexesById: Map[Vertex#EntityId, V] =
    vertexes
      .map(vertex =>
        vertex.id -> vertex
      )
      .toMap

  def getVertexById(id: Vertex#EntityId): Option[V] =
    vertexesById.get(id)

  def isEmpty: Boolean =
    vertexes.isEmpty

  @transient
  def nonEmpty: Boolean =
    vertexes.nonEmpty

  def links: Set[L]

  def getLinkById(id: Link#EntityId): Option[L]

  override def equals(obj: Any): Boolean = {
    obj match {
      case other: AnyRef =>
        eq(other)

      case _ =>
        false
    }
  }

  override def hashCode(): Int =
    System.identityHashCode(this)
}
