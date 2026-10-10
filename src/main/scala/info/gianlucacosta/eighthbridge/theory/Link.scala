package info.gianlucacosta.eighthbridge.theory

trait Link extends Entity

object Link {
  implicit class LinkExtensions[L <: Link](link: L) {
    def update(updatedLink: L): L =
      if (link == updatedLink)
        updatedLink
      else
        link
  }

  implicit class LinkSetExtensions[L <: Link](linkSet: Set[L]) {
    def updateLink(updatedLink: L): Set[L] =
      linkSet.map(_.update(updatedLink))
  }

  implicit class LinksByVertexPairMapExtensions[V <: Vertex, L <: Link](
                                                                         thisMap: Map[(V, V), Set[L]]
                                                                       ) {
    def flattenByVertex(vertexSelector: ((V, V)) => V): Map[V, Set[L]] =
      thisMap
        .view
        .groupBy { case (vertexPair, _) => vertexSelector(vertexPair) }
        .mapValues(
          _.map( {
              case ((_, _), link) => link
            })
            .flatten
            .toSet
        )
  }
}

