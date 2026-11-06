package info.gianlucacosta.eighthbridge.graphs.features

trait HasLabel {
  def label: String

  override def toString: String =
    label
}
