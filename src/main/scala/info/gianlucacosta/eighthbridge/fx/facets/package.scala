package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.core.Facet
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
    def copyAllFacetsFrom(source: AnyRef): T =
      self
        .copyCoreFacetsFrom(source)
        .copyFxFacetsFrom(source)
  }
}
