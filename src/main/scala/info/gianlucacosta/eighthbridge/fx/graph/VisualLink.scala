package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Link
import info.gianlucacosta.eighthbridge.graphs.facets._

trait VisualLink extends TimestampedPrototype
  with Link
  with HasSelected
  with HasStyleClasses
  with HasOptionalArcBarbs
  with HasHandles

