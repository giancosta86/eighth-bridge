package info.gianlucacosta.eighthbridge.core.directed

import info.gianlucacosta.eighthbridge.core.Link

trait Arc extends Link {
  def barbs: ArcBarbs
}
