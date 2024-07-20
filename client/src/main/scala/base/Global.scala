package base

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobal
import shared.model.User
import shared._
import shared.Ids._

def debug(msg: => String) = Logging.logger.debug(msg)
def info(msg: => String)  = Logging.logger.info(msg)
def warn(msg: => String)  = Logging.logger.warn(msg)
def error(msg: => String) = Logging.logger.error(msg)

@js.native
@JSGlobal("Math")
object Math extends js.Object {
  def random(): Double = js.native
}

trait Mgmt extends JsWrapper:
  def validUser = !User.isNil(Global.user)   
  def initUser   = setUser(User.nil)
  def initGlobal = Global.unique = randomString(8); setUser(User.nil)
                 
  def setUser(usr: User) = 
    Global.user = usr
    changeClass(gE(Auth_showLogin), validUser, "disabled")
    changeClass(gE(Auth_doLogout), !validUser, "disabled")
    changeClass(gE(Auth_LoginInfo), !validUser, "d-none")
    setHtml(gE(Auth_LoggedInAs), s"${Global.user.firstname} ${Global.user.lastname}")

  def resetUser = 
    Global.user = User.nil
    changeClass(gE(Auth_showLogin), validUser, "disabled")
    changeClass(gE(Auth_doLogout), !validUser, "disabled")
    changeClass(gE(Auth_LoginInfo), !validUser, "d-none")
    setHtml(gE(Auth_LoggedInAs), s"${Global.user.firstname} ${Global.user.lastname}")

  def getUser = Global.user  

  def getId   = Global.unique

  def setLang(lang: String) = Global.lang = lang  
  def setCsrf(csrf: String) = Global.csrf = csrf 

  def ucError(err: shared.model.Error): Unit =
    addClass(gE(Auth_Content), "d-none")
    removeClass(gE(Main_Content), "d-none")
    if usecases.Error.render(err) then
      setNavLink("Error")
    else   
      error(s"exec -> usecase:Error ${err}")


  def ucExec(usecase: String, param: String): Unit = 
    try
      addClass(gE(Auth_Content), "d-none")
      removeClass(gE(Main_Content), "d-none")
      if Global.ucMap(usecase).render(param) then
        setNavLink(usecase)
      else   
        error(s"exec -> usecase:${usecase} param:${param}")
    catch
      case e: Exception => error(s"exec -> usecase:${usecase} param:${param} not found")


object Global extends JsWrapper:
  import shared.model.User
  import usecases._
  import usecases.group1._
  import usecases.group2._
  import dialog._
  val localStoragePrefix = "App."
  var csrf = ""
  var lang = ""
  var user = User.nil
  var unique = ""

  // usecase map usecase name to usecase object   
  val ucMap = List(Home, Auth, Console, Error, 
                   DlgPrompt, 
                   ChatExample,
                   UseCase1Sub2, 
                   UseCase1Sub31, UseCase1Sub32,
                   UseCase2Sub11, UseCase2Sub12, 
                   UseCase2Sub211, UseCase2Sub212, UseCase2Sub213,
                   UseCase2Sub221, UseCase2Sub222, UseCase2Sub223)
                   .map(uc => uc.name -> uc).toMap  
