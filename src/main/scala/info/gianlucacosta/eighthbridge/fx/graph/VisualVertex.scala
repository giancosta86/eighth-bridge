package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.features.{Prototype, HasCenterSettable, HasSelectedSettable, HasStyleClassesSettable}

class VisualVertex extends Prototype
  with HasCenterSettable
  with HasSelectedSettable
  with HasStyleClassesSettable