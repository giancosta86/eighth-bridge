package info.gianlucacosta.eighthbridge.theory.features

trait Timestamped {
  protected def timestamp: Long

  protected def setTimestamp(timestamp: Long): this.type

  def updateTimestamp(): this.type =
    setTimestamp(System.nanoTime())

  def isNewerThan(other: Timestamped): Boolean =
    timestamp > other.timestamp
}
