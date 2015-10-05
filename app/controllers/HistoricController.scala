package controllers

import io.forward.yahoo.Finance
import play.api.mvc._

class HistoricController extends Controller {

  import scala.concurrent.ExecutionContext.Implicits.global

  def show(symbol: String) = Action.async {
    Finance.historicalData(symbol).map { response =>
      Ok(response)
    }
  }
}
