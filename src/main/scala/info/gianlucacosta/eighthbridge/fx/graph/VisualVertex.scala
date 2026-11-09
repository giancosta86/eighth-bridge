package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.facets.{HasCenterSettable, HasSelectedSettable, HasStyleClasses, Prototype, TimestampedPrototype}

//TODO! I could reduce even more the definition, by requiring Settable only in Controller methods!
trait VisualVertex extends TimestampedPrototype
  with Vertex
  with HasCenterSettable
  with HasSelectedSettable
  with HasStyleClasses
