package info.gianlucacosta.eighthbridge.fx.canvas.basic

import info.gianlucacosta.eighthbridge.graphs.point2point.visual.VisualLink

/**
  * Link dedicated to the "basic" package
  */
trait BasicLink extends VisualLink {
  def text: String

  def arrow: LinkArrow =
    LinkArrow.Default

  def handleRadius: LinkHandleRadius =
    LinkHandleRadius.Default

  override def toString: String =
    text
}
