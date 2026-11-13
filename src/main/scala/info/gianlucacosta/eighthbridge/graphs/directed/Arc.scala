package info.gianlucacosta.eighthbridge.graphs.directed

import info.gianlucacosta.eighthbridge.graphs.Link

trait Arc extends Link {
  def barbs: ArcBarbs
}
