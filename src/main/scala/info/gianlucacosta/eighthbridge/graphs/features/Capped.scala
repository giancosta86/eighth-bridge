package info.gianlucacosta.eighthbridge.graphs.features

trait Capped {
  def capacity: Weight

  def setCapacity(capacity: Weight): this.type
}
