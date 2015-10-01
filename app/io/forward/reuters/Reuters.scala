package io.forward.reuters

import domain.models.StockQuote
import org.jsoup._
import org.jsoup.nodes.Document
import play.api.libs.ws.WS

import scala.collection.JavaConverters._
import scala.concurrent.{ExecutionContext, Future}
import scalaz.Scalaz._

object Reuters {

  import play.api.Play.current

  private val baseUrl = "http://uk.reuters.com/business/markets/index?symbol=."

  private def toDouble(value: String) = value.replace(",", "").toDouble

  private [this] def extractChangePercentage(document: Document): Double = {
    val changeText = document.select(".dataParenthetical").asScala.headOption map (_.text)
    changeText.getOrElse("").replace("(", "").replace(")", "").replace("%", "").toDouble
  }

  private [this] def extractQuote(document: Document): Option[StockQuote] = {
    document.select(".dataHeader").asScala.toVector map (node => toDouble(node.text)) match {
      case Vector(price, change, open, _) =>
        val percentageChange = extractChangePercentage(document)
        Some(StockQuote(price, change, percentageChange, open))
      case _ => None
    }
  }

  def getQuote(symbol: String)(implicit ec: ExecutionContext): Future[Option[StockQuote]] = {
    WS.url(baseUrl |+| symbol).get() map { response =>
      extractQuote(Jsoup.parse(response.body))
    }
  }
}
