package info.gianlucacosta.eighthbridge.core

package object facets {
  type Timestamp =
    Long

  type Weight =
    Double

  implicit class CoreFacetsExtensions[T <: AnyRef](self: T) {
    def copyCoreFacetsFrom(source: AnyRef): T = {
      var result: T =
        self

      Facet.as[HasBarbsSettable](result).foreach(facetedResult =>
        Facet.as[HasBarbs](source).foreach(facetedSource => {
          result =
            facetedResult.setBarbs(facetedSource.barbs).asInstanceOf[T]
        })
      )

      Facet.as[HasLabelSettable](result).foreach(facetedResult =>
        Facet.as[HasLabel](source).foreach(facetedSource => {
          result =
            facetedResult.setLabel(facetedSource.label).asInstanceOf[T]
        })
      )

      Facet.as[HasNameSettable](result).foreach(facetedResult =>
        Facet.as[HasName](source).foreach(facetedSource => {
          result =
            facetedResult.setName(facetedSource.name).asInstanceOf[T]
        })
      )

      Facet.as[HasSelectedSettable](result).foreach(facetedResult =>
        Facet.as[HasSelected](source).foreach(facetedSource => {
          result =
            facetedResult.setSelected(facetedSource.selected).asInstanceOf[T]
        })
      )

      Facet.as[HasStyleClassesSettable](result).foreach(facetedResult =>
        Facet.as[HasStyleClasses](source).foreach(facetedSource => {
          result =
            facetedResult.setStyleClasses(facetedSource.styleClasses).asInstanceOf[T]
        })
      )

      Facet.as[HasWeightBoundedSettable](result).foreach(facetedResult =>
        Facet.as[HasWeightBounded](source).foreach(facetedSource => {
          result =
            facetedResult.setWeight(
              demand=
                facetedSource.demand,
              weight=
                facetedSource.weight,
              capacity=
                facetedSource.capacity
            ).asInstanceOf[T]
        })
      )

      result
    }
  }
}
