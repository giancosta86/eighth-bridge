package info.gianlucacosta.eighthbridge.graphs.features

trait HasSettableTimestamp extends ConcreteCloneable with HasTimestamp {
  private var _timestamp: Timestamp =
    Timestamp.now

  override def timestamp: Timestamp =
    _timestamp

  def updateTimestamp(): this.type = {
    val result =
      clone()

    result._timestamp =
      Timestamp.now

    result
  }
}

