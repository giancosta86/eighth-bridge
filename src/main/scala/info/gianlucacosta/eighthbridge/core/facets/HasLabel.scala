package info.gianlucacosta.eighthbridge.core.facets

trait HasLabel {
  def label: String

  override def toString: String =
    label
}
