package services

import org.scalajs.dom
import org.scalajs.dom.ext.Ajax
import scala.concurrent.Future
import scala.scalajs.concurrent.JSExecutionContext.Implicits.queue
import upickle.default._

import base.Global
import shared._
import shared.model._

trait ComWrapper: 

  // postData - basic wrapper routine for a Ajax post request 
  def postData(route: String, params: List[(String,String)], data: String="", contType: String = "text/plain; charset=utf-8"): FuEiErr[String] = 
    val name = route.split("/").lastOption.getOrElse("postData")
    Ajax.post(genPath(route, params), data, headers = Map("Content-Type"->s"${contType}", "Csrf-Token" -> Global.csrf))
      .map(_.responseText).map(content => Right(content))
      .recover({
        // Recover from a failed error code into a successful future
        case dom.ext.AjaxException(req) => Left(parseError(req.responseText, name))   
        case _: Throwable               => Left(Error("err00001.ajax.postData", s"${route}/${params.mkString(":")}", "request status unknown", name))    
      })

  // postJson - basic wrapper routine for a Ajax post request 
  def postJson[T](route: String, params: List[(String,String)], data: String="", contType: String = "text/plain; charset=utf-8")(using r: Reader[T]): FuEiErr[T] = 
    val name = route.split("/").lastOption.getOrElse("postData")
    Ajax.post(genPath(route, params), data, headers = Map("Content-Type"->s"${contType}", "Csrf-Token" -> Global.csrf))
      .map(_.responseText).map(content => parseJson[T](content))
      .recover({
        // Recover from a failed error code into a successful future
        case dom.ext.AjaxException(req) => Left(parseError(req.responseText, name))   
        case _: Throwable               => Left(Error("err00001.ajax.postData", s"${route}/${params.mkString(":")}", "request status unknown", name))    
      })


  /** getData - basic wrapper for get requests   
   * @return either an error or a string 
   */
  def getData(route: String, params: List[(String,String)]=List()): FuEiErr[String] =
    val name = route.split("/").lastOption.getOrElse("getData")
    Ajax.get(genPath(route, params)).map(_.responseText)
      .map(content => Right(content))
      .recover({
        case dom.ext.AjaxException(req) => Left(parseError(req.responseText, name)) 
        case _: Throwable               => Left(Error("err00009.ajax.getData", s"${genPath(route, params)}", "request status unknown", name))   
    })


  /** getJson - basic wrapper for get requests   
   * @return either an error or a string 
   */
  def getJson[T](route: String, params: List[(String,String)]=List())(using r: Reader[T]): FuEiErr[T] =
    val name = route.split("/").lastOption.getOrElse("getJson")
    Ajax.get(genPath(route, params)).map(_.responseText)
      .map(content => parseJson[T](content))
      .recover({
        case dom.ext.AjaxException(req) => Left(parseError(req.responseText, name)) 
        case _: Throwable               => Left(Error("err00012.ajax.getJson", s"${genPath(route, params)}", "request status unknown", name))   
    })


  // genPath - encodes params to URL encoded 
  def genPath(route: String, params: List[(String,String)]): String = 
    val urlParams = params.map(x => s"${x._1}=${x._2}").mkString("&") 
    if (params.isEmpty) route else s"${route}?${urlParams}"
     