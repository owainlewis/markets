package io.forward.reuters

import org.jsoup._
import org.jsoup.nodes.Document
import play.api.libs.ws.WS

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._

case class ReutersStockQuote(
  price            : Double,
  changePoints     : Double,
  changePercentage : Double,
  open             : Double)

object Reuters {

  import play.api.Play.current

  private val baseUrl = "http://uk.reuters.com/business/markets/index?symbol=."

  private def toDouble(value: String) = value.replace(",", "").toDouble

  private [this] def extractChangePercentage(document: Document): Double = {
    val changeText = document.select(".dataParenthetical").asScala.headOption map (_.text)
    changeText.getOrElse("").replace("(", "").replace(")", "").replace("%", "").toDouble
  }

  private [this] def extractQuote(document: Document): Option[ReutersStockQuote] = {
    document.select(".dataHeader").asScala.toVector map (node => toDouble(node.text)) match {
      case Vector(price, change, open, _) =>
        val percentageChange = extractChangePercentage(document)
        Some(ReutersStockQuote(price, change, open, percentageChange))
      case _ => None
    }
  }

  def getQuote(symbol: String)(implicit ec: ExecutionContext): Future[Option[ReutersStockQuote]] = {
    WS.url(baseUrl |+| symbol).get() map { response =>
      extractQuote(Jsoup.parse(response.body))
    }
  }
}
