package usecases.group2

import base._

object UseCase2Sub212 extends UseCase with JsWrapper:
  
  def render(param: String = ""): Boolean = 
    setMain(s"""<div class='d-flex mt-5 justify-content-center'><h5>${name}</h5></div>""")




