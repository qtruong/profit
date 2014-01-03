/*---------------------------------------------------------------------------
| $Id: SignalFieldConstant.java,v 1.5 2013/12/31 14:53:36 quoc Exp $
| Copyright (c) 2013 Quoc T. Truong. All Rights Reserved.
|--------------------------------------------------------------------------*/
package com.qtt.db.common;

import java.util.Map;
import com.qtt.tool.util.MapUtil;

public class SignalFieldConstant
{
  public final static String DATA_TYPE_STRING = "String";
  public final static String DATA_TYPE_INTEGER = "Integer";
  public final static String DATA_TYPE_LONG = "Long";
  public final static String DATA_TYPE_FLOAT = "Float";
  public final static String DATA_TYPE_DOUBLE = "Double";

  public final static String CREATE_DATE = "create_date";

  public final static String[] FIELD_LIST =
  {
    "id",                   DATA_TYPE_INTEGER,
    CREATE_DATE,            DATA_TYPE_STRING,
    "adx",                  DATA_TYPE_DOUBLE,
    "di_pos",               DATA_TYPE_DOUBLE,
    "di_neg",               DATA_TYPE_DOUBLE,
    "ma200",                DATA_TYPE_DOUBLE,
    "ma150",                DATA_TYPE_DOUBLE,
    "ma100",                DATA_TYPE_DOUBLE,
    "ma50",                 DATA_TYPE_DOUBLE,
    "ma20",                 DATA_TYPE_DOUBLE,
    "ma10",                 DATA_TYPE_DOUBLE,
    "ma5",                  DATA_TYPE_DOUBLE,
    "ma4",                  DATA_TYPE_DOUBLE,
    "ma3",                  DATA_TYPE_DOUBLE,
    "ema200",               DATA_TYPE_DOUBLE,
    "ema50",                DATA_TYPE_DOUBLE,
    "ema20",                DATA_TYPE_DOUBLE,
    "ema10",                DATA_TYPE_DOUBLE,
    "ema5",                 DATA_TYPE_DOUBLE,
    "ema4",                 DATA_TYPE_DOUBLE,
    "dema200",              DATA_TYPE_DOUBLE,
    "dema50",               DATA_TYPE_DOUBLE,
    "dema20",               DATA_TYPE_DOUBLE,
    "dema10",               DATA_TYPE_DOUBLE,
    "dema5",                DATA_TYPE_DOUBLE,
    "dema4",                DATA_TYPE_DOUBLE,
    "tema200",              DATA_TYPE_DOUBLE,
    "tema50",               DATA_TYPE_DOUBLE,
    "tema20",               DATA_TYPE_DOUBLE,
    "tema10",               DATA_TYPE_DOUBLE,
    "tema5",                DATA_TYPE_DOUBLE,
    "tema4",                DATA_TYPE_DOUBLE,
    "volume",               DATA_TYPE_INTEGER,
    "volume20",             DATA_TYPE_INTEGER,
    "avg_volume30",         DATA_TYPE_INTEGER,
    "price",                DATA_TYPE_DOUBLE,
    "price_high",           DATA_TYPE_DOUBLE,
    "price_low",            DATA_TYPE_DOUBLE,
    "price_open",           DATA_TYPE_DOUBLE,
    "price_prev",           DATA_TYPE_DOUBLE,
    "range90",              DATA_TYPE_DOUBLE,
    "atr90",                DATA_TYPE_DOUBLE,
    "stochastic",           DATA_TYPE_DOUBLE,
    "slow_stochastic_k",    DATA_TYPE_DOUBLE,
    "slow_stochastic_d",    DATA_TYPE_DOUBLE,
    "ar10",                 DATA_TYPE_DOUBLE,
    "pierce20",             DATA_TYPE_INTEGER,
    "quantum",              DATA_TYPE_STRING,
    "landry_pattern",       DATA_TYPE_STRING,
    "pattern",              DATA_TYPE_STRING,
    "rsi2",                 DATA_TYPE_DOUBLE,
    "rsi4",                 DATA_TYPE_DOUBLE,
    "open_rsi2",            DATA_TYPE_DOUBLE,
    "open_rsi4",            DATA_TYPE_DOUBLE,
    "crsi",                 DATA_TYPE_DOUBLE,
    "bollinger_percent",    DATA_TYPE_DOUBLE,
    "channel",              DATA_TYPE_DOUBLE,
    "rchannel",             DATA_TYPE_DOUBLE,
    "rslope40",             DATA_TYPE_DOUBLE,
    "rslope80",             DATA_TYPE_DOUBLE,
    "break",                DATA_TYPE_INTEGER,
    "high52",               DATA_TYPE_DOUBLE,
    "low52",                DATA_TYPE_DOUBLE,
    "volatility100",        DATA_TYPE_DOUBLE,
    "up_down",              DATA_TYPE_INTEGER,
    "earning_trend_q",      DATA_TYPE_INTEGER,
    "earning_trend_y",      DATA_TYPE_INTEGER,
    "cci",                  DATA_TYPE_DOUBLE,
  };

  public final static Map FIELD_TABLE = MapUtil.createLinkedHashMap(FIELD_LIST);
}
