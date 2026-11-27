package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.core.{Facet, Graph, Link, Vertex}
import info.gianlucacosta.eighthbridge.core.facets.CoreFacetsExtensions

package object facets {
  implicit class FxFacetsExtensions[T <: AnyRef](self: T) {
    def copyFxFacetsFrom(source: AnyRef): T = {
      var result: T =
        self

      Facet.as[HasCenterSettable](result).foreach(facetedResult =>
        Facet.as[HasCenter](source).foreach(facetedSource => {
          result =
            facetedResult.setCenter(facetedSource.center).asInstanceOf[T]
        })
      )

      Facet.as[HasDimensionSettable](result).foreach(facetedResult =>
        Facet.as[HasDimension](source).foreach(facetedSource => {
          result =
            facetedResult.setDimension(facetedSource.dimension).asInstanceOf[T]
        })
      )

      Facet.as[HasHandlesSettable](result).foreach(facetedResult =>
        Facet.as[HasHandles](source).foreach(facetedSource => {
          result =
            facetedResult.setHandles(facetedSource.handles).asInstanceOf[T]
        })
      )

      Facet.as[HasOptionalCustomLabelCenterSettable](result).foreach(facetedResult =>
        Facet.as[HasOptionalCustomLabelCenter](source).foreach(facetedSource => {
          result =
            facetedResult.setCustomLabelCenter(facetedSource.customLabelCenter).asInstanceOf[T]
        })
      )

      result
    }
  }

  implicit class AllFacetsExtensions[T <: AnyRef](self: T) {
    def copyFacetsFrom(source: AnyRef): T =
      self
        .copyCoreFacetsFrom(source)
        .copyFxFacetsFrom(source)
  }

  implicit class GraphExtensions[SourceV <: Vertex, SourceL <: Link](
     self: Graph[SourceV, SourceL]
   ) {
    def mapWithFacetsTo[TargetV <: Vertex, TargetL <: Link](
       targetVertexCreator: (SourceV) => TargetV,
       targetLinkCreator: (SourceV, SourceV, SourceL) => TargetL
     ): Graph[TargetV, TargetL] = {
      var targetGraph: Graph[TargetV, TargetL] = (
        if (self.isDirected)
          Graph.createDirected()
        else
          Graph.createUndirected()
        ).asInstanceOf[Graph[TargetV, TargetL]]

      var targetVertexBySourceVertex: Map[SourceV, TargetV] =
        Map[SourceV, TargetV]()

      self.vertexes.foreach(sourceVertex => {
        val targetVertex =
          targetVertexCreator(sourceVertex)
            .copyFacetsFrom(sourceVertex)

        targetVertexBySourceVertex +=
          sourceVertex -> targetVertex

        targetGraph =
          targetGraph.addVertex(targetVertex)
      })

      self.links.foreach(sourceLink => {
        val (firstSourceAnchor, secondSourceAnchor): (SourceV, SourceV) =
          self.anchorsByLink(sourceLink).toPair

        val targetAnchors =
          (
            targetVertexBySourceVertex(firstSourceAnchor),
            targetVertexBySourceVertex(secondSourceAnchor)
          )

        val targetLink: TargetL =
          targetLinkCreator(firstSourceAnchor, secondSourceAnchor, sourceLink)
            .copyFacetsFrom(sourceLink)

        targetGraph =
          targetGraph.addLink(targetAnchors, targetLink)
      })

      targetGraph
    }
  }
}
