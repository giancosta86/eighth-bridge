package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.facets.{HasCenter, HasSelected, HasStyleClasses, TimestampedPrototype}

//TODO! I could reduce even more the definition, by requiring Settable only in Controller methods!
trait VisualVertex extends TimestampedPrototype
  with Vertex
  with HasCenter
  with HasSelected
  with HasStyleClasses
