package info.gianlucacosta.eighthbridge.fx.graph

import info.gianlucacosta.eighthbridge.graphs.Vertex
import info.gianlucacosta.eighthbridge.graphs.features.{ConcreteCloneable, HasCenterSettable, HasSelectedSettable, HasStyleClassesSettable}

class VisualVertex extends ConcreteCloneable
  with HasCenterSettable
  with HasSelectedSettable
  with HasStyleClassesSettable