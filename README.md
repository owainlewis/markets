# Markets

An API for financial markets.

**Warning** The current method of data extraction is probably illegal (the markets don't want you to have access to data they can charge extortionate fees for), so don't use this for commercial projects.

## Motivation

Accessing easy to use stock market data is quite a difficult task. Not only is access to data expensive, the companies
that do offer API services tend to have terrible APIs.

This is an attempt at building an easy to use API for accessing stock market data in "near real time".

## API

### Index

A stock index or stock market index is a measurement of the value of a section of the stock market. It is computed from the prices of selected stocks (typically a weighted average). It is a tool used by investors and financial managers to describe the market, and to compare the return on specific investments.

#### Look up all major stock indexes

```
GET /api/index
```

#### Get the current price and information for an index 

(e.g DJI is the symbol for the Dow Jones Index)

```
GET /api/index/:symbol
```

#### Sample response

```json
{  
   "symbol":"DJI",
   "name":"Dow Jones Industrial Average",
   "quote":{  
      "price":15871.35,
      "changePoints":-588.4,
      "changePercentage":-3.57,
      "open":16459.8
   }
}
```
