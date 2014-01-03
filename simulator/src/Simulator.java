package com.qtt.app.stock.simulator;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import org.apache.logging.log4j.Level;
import com.qtt.tool.util.Performance;
import com.qtt.db.DB;
import com.qtt.db.DBHandler;
import com.qtt.db.DBReader;
import com.qtt.db.fs.DBFS;
import com.qtt.db.param.SignalHistoryParam;

public class Simulator implements DBHandler 
{
  Trader mTrader;
  Performance perf = new Performance();
  SimSetting mSimSetting = new SimSetting();
  HashSet mIdSet = new HashSet();

  public static void main(String[] arg)
  {
    new Simulator().go(arg);
  }

  // Technical signal have already been precalculated and stored.
  // Iterate the technical signal each day from a specified date to the 
  // present. A trader object will buy/sell stocks according to the 
  // criteria set in the configuration. 
  public void go(String[] arg)
  {
    if (arg.length < 1)
    {
      System.err.println("Usage: Simulator <conf file>");
      System.exit(1);
    }

    // Reading configuration file.
    ConfigLoader loader = new ConfigLoader();
    loader.loadConfigFile(arg[0], mSimSetting);
    Util.mLogger.info("start date: {}", mSimSetting.mStartDate);
 
    // Initializing 
    Transaction.instance().setSimSetting(mSimSetting);
    mTrader = new Trader(mSimSetting);

    // Fetch signal history a day at a time.  handleData() will be used
    // to handle the data.
    DB db = new DBFS();
    DBReader reader = db.getReader("SignalHistoryReader");
    perf.begin("handleData()");
    SignalHistoryParam param = new SignalHistoryParam();
    param.mStartDate = mSimSetting.mStartDate;
    param.mDBHandler = this;
    reader.read(param);

    // Show results of the simulation
    mTrader.show();
    Transaction.instance().showAll();
  }

  // Callbacks from DBReader
  // Input: ArrayList of signal data
  public void handleData(Object data)
  {
    ArrayList signalList = (ArrayList) data;

    ridDup(signalList);
    mTrader.input(signalList);
    LookBack.instance().add(signalList);
    perf.end("handleData()");

    if (Util.mLogger.isEnabled(Level.INFO))
    {
      StringWriter swriter = new StringWriter();
      perf.showResult(new PrintWriter(swriter));
      Util.mLogger.info(swriter.toString());
    }

    perf.begin("handleData()");
  }

  void ridDup(ArrayList signalList)
  {
    mIdSet.clear();
    int size = signalList.size();
    for (int i=size-1; i>=0; i--)
    {
      Map signalData = (Map) signalList.get(i);
      Integer id = (Integer) signalData.get("id");
      if (mIdSet.contains(id))
      {
        signalList.remove(i);
      }
      else
      {
        mIdSet.add(id);
      }
    }
    mIdSet.clear();
  }
}
