package shared.model

import scala.concurrent.Future

import upickle.default.{ReadWriter => RW, macroRW}
import upickle.default._

case class Error(msgCode:String, var in1:String="", var in2:String="", var callStack: String=""):
  def equal2Code(code: String): Boolean = { this.msgCode == code }
  def is(code: String): Boolean = { this.msgCode == code }
  def add(func: String): Error = { callStack = s"${func}:${callStack}"; this} 
  def isDummy  = (msgCode == "")

object Error: 
  implicit val rw: RW[Error] = macroRW
  def apply[T](msgCode: String, in: T) = new Error(msgCode, in.toString(), "", "")
  def apply[T,U](msgCode: String, in1: T, in2: U) = new Error(msgCode, in1.toString(), in2.toString(), "")
  def apply[T,U](msgCode: String, in1: T, in2: U, callStack: String) = new Error(msgCode, in1.toString(), in2.toString(), callStack)
  def dummy = Error("","","","")


def parseError(in: String, func: String): Error =
  try read[Error](in)
  catch { case e: Throwable => Error("err00006.parseJson", e.getMessage, in.take(10)).add(func) }


