import mill._
import $ivy.`com.lihaoyi::mill-contrib-playlib:`,  mill.playlib._

object prefcards extends PlayModule with SingleModule {

  def scalaVersion = "2.13.15"
  def playVersion = "3.0.6"
  def twirlVersion = "2.0.1"

  object test extends PlayTests
}
