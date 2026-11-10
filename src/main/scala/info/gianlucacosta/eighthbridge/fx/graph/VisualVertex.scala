package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.facets.{HasCenter, HasCenterSettable, HasSelected, HasSelectedSettable, HasStyleClasses, Prototype, TimestampedPrototype}

//TODO! I could reduce even more the definition, by requiring Settable only in Controller methods!
trait VisualVertex extends TimestampedPrototype
  with Vertex
  with HasCenter
  with HasSelected
  with HasStyleClasses
