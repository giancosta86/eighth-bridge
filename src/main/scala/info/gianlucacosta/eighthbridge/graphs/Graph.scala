package info.gianlucacosta.eighthbridge.graphs

trait Graph[V <: Vertex, L <: Link] {
  val vertexes: Set[V]

  val vertexesById: Map[Vertex#Id, V]

  def getVertexById(id: Vertex#Id): Option[V]

  val isEmpty: Boolean

  val nonEmpty: Boolean

  def addVertexes(vertexesToAdd: Set[V]): this.type

  def addVertexes(vertexesToAdd: V*): this.type

  def addVertex(vertexToAdd: V): this.type

  def updateVertexes(updatedVertexes: Set[V]): this.type

  def updateVertexes(updatedVertexes: V*): this.type

  def updateVertex(updatedVertex: V): this.type

  def removeVertexes(vertexesToRemove: Set[V]): this.type

  def removeVertexes(vertexesToRemove: V*): this.type

  def removeVertex(vertexToRemove: V): this.type

  val links: Set[L]

  val linksById: Map[Link#Id, L]

  def getLinkById(id: Link#Id): Option[L]

  def getLinksBetween(firstAnchor: V, secondAnchor: V): Set[L]

  def getAnchorPair(link: L): Option[(V, V)]

  def addLink(firstAnchor: V, secondAnchor: V, linkToAdd: L): this.type

  def addLink(anchorPair: (V, V), linkToAdd: L): this.type

  def updateLinks(updatedLinks: Set[L]): this.type

  def updateLinks(updatedLinks: L*): this.type

  def updateLink(updatedLink: L): this.type

  def removeLinks(linksToRemove: Set[L]): this.type

  def removeLinks(linksToRemove: L*): this.type

  def removeLink(linkToRemove: L): this.type

  val neighborsByVertex: Map[V, Set[V]]

  def getNeighbors(vertex: V): Set[V]

  val connectedVertexes: Set[V]

  val isolatedVertexes: Set[V]

  val connectedLinksByVertex: Map[V, Set[L]]

  def getConnectedLinks(vertex: V): Set[L]

  val isCyclic: Boolean
}
