package info.gianlucacosta.eighthbridge.fx

import info.gianlucacosta.eighthbridge.core.Vertex
import info.gianlucacosta.eighthbridge.core.facets.TimestampedPrototype
import info.gianlucacosta.eighthbridge.fx.facets.HasCenter

trait VisualVertex extends TimestampedPrototype
  with Vertex
  with HasCenter
