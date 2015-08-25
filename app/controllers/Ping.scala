package controllers

import play.api.mvc._

class Ping extends Controller {

  def index = Action { Ok("OK") }
}
