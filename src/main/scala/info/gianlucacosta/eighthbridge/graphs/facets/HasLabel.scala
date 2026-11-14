package info.gianlucacosta.eighthbridge.graphs.facets

trait HasLabel {
  def label: String

  override def toString: String =
    label
}
