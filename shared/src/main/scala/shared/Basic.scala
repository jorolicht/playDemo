package shared

import scala.concurrent.Future
import shared.model.Error
import java.util.Random

import upickle.default.{ReadWriter => RW, macroRW}
import upickle.default._

type EiErr[T] = Either[Error, T]
type FuEiErr[T] = Future[Either[Error, T]]

/** generate random string with length
  * 
  */
def randomString(length: Int): String =
  val rand = new Random()
  val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray
  val sb = new StringBuilder

  for (_ <- 0 until length)
    val randomIndex = rand.nextInt(chars.length)
    sb.append(chars(randomIndex))
  sb.toString()   

def ite[T](cond: Boolean, valA: => T, valB: => T): T = if (cond) valA else valB

inline def parseJson[T](x: String)(using r: Reader[T]): Either[Error, T] = {
  try if x=="" then Left(Error("err00006.parseJson", "empty string")) else Right(read[T](x))
  catch { case e: Throwable => Left(Error("err00006.parseJson", e.getMessage, x.take(10))) }
}

inline def toJson[T](x: T)(using w: Writer[T]): String = write[T](x)

extension (str: String)
  def toTuple(sep: String=":"): Tuple2[String, String] = 
    val x = str.split(sep)
    if x.length != 2 then ("","") else (x(0),x(1))

  def toError(func: String): Error = 
    try read[Error](str)
    catch { case e: Throwable => Error("err00006.parseJson", e.getMessage, str.take(10)).add(func) }

  def to[T]()(using r: Reader[T]): Either[Error, T] = {
    try if str == "" then Left(Error("err00006.parseJson", "empty string")) else Right(read[T](str))
    catch { case e: Throwable => Left(Error("err00006.parseJson", e.getMessage, str.take(10))) }
  }    

  