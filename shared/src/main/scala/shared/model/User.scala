package shared.model

import upickle.default._
import upickle.default.{ReadWriter => RW, macroRW}

case class User(firstname: String, lastname: String, var email: String, var password: String="",
                var picUrl: String = "", 
                var locale: String = "", 
                var verified: Boolean = false,
                var request: Long = 0L,
                var id: Long =0L):
  def verifyInfo = write[(Long,String,Long)]((id,email,request))


object User:
  implicit val rw: RW[User] = macroRW
  def nil = User("","","","","","",false,0L,0L)
  def isNil(user: User) = (user.id == 0L)