package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.facets.{Prototype, HasHandlesSettable, HasOptionalArcBarbsSettable, HasSelectedSettable, HasStyleClassesSettable, HasTimestampSettable}

class VisualLink extends Prototype
  with Link
  with HasSelectedSettable
  with HasHandlesSettable
  with HasStyleClassesSettable
  with HasOptionalArcBarbsSettable