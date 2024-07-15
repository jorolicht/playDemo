package base

import upickle.default._
import scala.scalajs.js
import scala.collection.mutable.Map

import shared.model.Error
import shared.Ids._

// atou - decode base64 encoded string to utf
def atou(text: String) = js.Dynamic.global.atou(text.asInstanceOf[js.Any]).asInstanceOf[String]
// utob - encode string to base64
def utob(b64: String) = js.Dynamic.global.utob(b64.asInstanceOf[js.Any]).asInstanceOf[String]

object Messages extends JsWrapper:
  private var messages: Map[String, String] = Map (""->"")

  def initMsg() = 
    try 
      var msgData = atou(getData(gE(Main_Messages), "messages", ""))
      if (msgData == "") then msgData = 
        info("initMsg -> use local messages")
        getLocalStorage("messages") 
      else 
        setLocalStorage("messages", msgData)      
      messages = read[Map[String, String]](msgData) 
    catch { case _: Throwable => error(s"initMsg") } 


  /** getMsg
    *
    * @param key of message
    * @param args inserts to message
    */
  def getMsg(key: String, args: String*): String = 
    try 
      if (!messages.isDefinedAt(key)) initMsg()
      var m = messages(key)
      args.zipWithIndex.foreach{ case(x,i) => m = m.replace(s"{${i}}",x) }
      m
    catch { case _: Throwable => error(s"getMsg -> key:${key} args:${args.mkString(":")} not found"); key }


  /** getErr
    *
    * @param err
    */
  def getErr(err: Error): String = 
    try 
      if (!messages.isDefinedAt(err.msgCode)) initMsg()
      messages(err.msgCode).replace(s"{0}", err.in1).replace(s"{1}", err.in2)
    catch { case _: Throwable => error(s"getErr -> key:${err.msgCode} in1:${err.in1} in2:${err.in2} not found"); err.msgCode }     
