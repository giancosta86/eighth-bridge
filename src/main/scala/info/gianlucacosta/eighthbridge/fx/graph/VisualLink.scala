package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.facets.{HasHandlesSettable, HasOptionalArcBarbs, HasSelectedSettable, HasStyleClasses, Prototype}

trait VisualLink extends Prototype
  with Link
  with HasSelectedSettable
  with HasStyleClasses
  with HasOptionalArcBarbs
  with HasHandlesSettable
