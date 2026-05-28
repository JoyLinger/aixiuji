package com.jt.customer.controller;

import com.alibaba.fastjson.JSONObject;
import com.jt.customer.entity.*;
import com.jt.customer.service.StatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@Controller
public class StatController {
  private final Class<StatController> clazz = StatController.class;
  private final Logger LOG = LoggerFactory.getLogger(clazz);
  private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  @Autowired
  StatService statService;

  @RequestMapping("/filter/stats/incomeBase")
  public String income(Integer durPeriod, Integer durVal, Model model) {
    // 第三个返回值为java的SimpleDateFormat格式
    String[] args = formatByDur(durPeriod, durVal);
    String dateFormat4sql = args[0];
    String startDay = args[1];
    List<IncomeInterface> incomes = statIncomes(dateFormat4sql, startDay, durVal);
    model.addAttribute("incomes", incomes);
    if(durPeriod != null && durPeriod == 3) {
      List<IncomeInterface> monthlyIncomes = statIncomes("%Y-%m", startDay.substring(0, 4) + "-01", durVal);
      model.addAttribute("monthlyIncomes", monthlyIncomes);
    }
    return "filter/stats/showIncomes";
  }

  @RequestMapping("/filter/stats/income4month")
  public String income4month(Model model) {
    List<Income> incomes = null;
    try {
      incomes = statService.income4month();
    } catch (IllegalAccessException | InstantiationException e) {
      e.printStackTrace();
    }
    model.addAttribute("incomes", incomes);
    return "filter/stats/showIncomes4month";
  }

  @RequestMapping("/filter/stats/customer4month")
  public String customer4month(Model model) {
    List<Customer> customers = statService.customer4month();
    model.addAttribute("customers", customers);
    return "filter/stats/showCustomers";
  }

  @RequestMapping("/filter/stats/incomeHome")
  public String incomeHome() {
    return "filter/stats/incomeChart";
  }

  public String[] formatByDur(Integer durPeriod, Integer durVal){
    LOG.info("durPeriod={}, defaultDays={}", durPeriod, durVal);
    LocalDate startDate = LocalDate.now().minusDays(durVal);
    int startYear = startDate.getYear();
    int startMonth = startDate.getMonthValue();
    String dateFormat4sql = "%Y-%m-%d", dateFormat4java = "yyyy-MM-dd", startDay;
    switch (durPeriod) {
      case 1:
        dateFormat4sql = "%Y-%m-%d";
        dateFormat4java = "yyyy-MM-dd";
        startDay = startDate.toString();
        break;
      case 2:
        dateFormat4sql = "%Y-%m";
        dateFormat4java = "yyyy-MM";
        startDay = startYear + "-" + (startMonth > 9 ? startMonth : "0" + startMonth);
        break;
      case 3:
        dateFormat4sql = "%Y";
        dateFormat4java = "yyyy";
        startDay = startYear + "";
        break;
      default:
        LOG.warn("不合法的统计周期:{},默认按天统计", durPeriod);
        startDay = startDate.toString();
    }
    LOG.info("startDay={}", startDay);
    return new String[]{dateFormat4sql, startDay, dateFormat4java};
  }
  public List<IncomeInterface> statIncomes(String format, String startDay, Integer durVal){
    List<IncomeInterface> incomes = new ArrayList<>();
    incomes = durVal <= 0 ?
            statService.statIncomeByDateFormat(format)
            : statService.statIncomeByDateFormatAndStartDay(format, startDay);
    return incomes;
  }
  /**
   * 在使用 @RequestMapping后，返回值通常解析为跳转路径，
   * 但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，而是直接写入 HTTP response body 中。
   * 比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据。
   *
   * @param durPeriod 统计周期: 天1/月2/年3
   * @param durVal 时间范围: 显示最近几天的数据，默认显示最近30天数据，0或负数表示显示全部数据
   * @return 统计结果数据
   */
  @RequestMapping("/filter/stats/income")
  @ResponseBody
  public JSONObject incomeChart(Integer durPeriod, Integer durVal) {
    String title = "收入统计";
    List<String> legends = new ArrayList<>();
    legends.add("收入");
    legends.add("顾客人数");
    List<String> xDataList = new ArrayList<>();
    List<ECharts_yAxis> yAxisList = new ArrayList<>();
    yAxisList.add(new ECharts_yAxis("收入","left","元"));
    yAxisList.add(new ECharts_yAxis("顾客人数","right","人"));
    List<EChartsSeries> seriesList = new ArrayList<>();
    Map<String, List<Integer>> seriesMap = new HashMap<>();
    // 第三个返回值为java的SimpleDateFormat格式
    String[] args = formatByDur(durPeriod, durVal);
    String dateFormat4sql = args[0];
    String startDay = args[1];
    String dateFormat4java = args[2];
    List<IncomeInterface> incomes = statIncomes(dateFormat4sql, startDay, durVal);
    
    // 判断是否按年统计
    boolean isYearStat = (durPeriod != null && durPeriod == 3);
    
    if(isYearStat && incomes != null && !incomes.isEmpty()) {
      // 按年统计：获取所有年份
      List<String> years = new ArrayList<>();
      for(IncomeInterface income : incomes) {
        String date = income.getDate();
        if(date.length() == 4) {
          years.add(date);
        } else if(date.length() == 7) {
          String year = date.substring(0, 4);
          if(!years.contains(year)) {
            years.add(year);
          }
        }
      }
      
      // x轴为年份
      for(String year : years) {
        xDataList.add(year);
      }
      
      // 为每个年份获取12个月份的数据
      // 收入系列：每个年份的12个月份堆叠
      for(int month = 1; month <= 12; month++) {
        String monthName = month + "月_收入";
        List<Integer> monthDataList = new ArrayList<>();
        for(String year : years) {
          List<IncomeInterface> monthlyIncomes = statIncomes("%Y-%m", year + "-01", -1);
          int monthValue = 0;
          for(IncomeInterface mi : monthlyIncomes) {
            String miDate = mi.getDate();
            String miYearStr = miDate.substring(0, 4);
            String miMonthStr = miDate.substring(miDate.length() - 2);
            int miYear = Integer.parseInt(miYearStr);
            int miMonth = Integer.parseInt(miMonthStr);
            if(miYear == Integer.parseInt(year) && miMonth == month) {
              monthValue = mi.getIncome();
              break;
            }
          }
          monthDataList.add(monthValue);
        }
        EChartsSeries incomeSeries = new EChartsSeries(monthName, monthDataList);
        incomeSeries.setyAxisIndex(0);
        incomeSeries.setStack("incomeStack");
        incomeSeries.setType("bar");
        incomeSeries.setBarGap("0%");
        incomeSeries.setBarWidth("40%");
        seriesList.add(incomeSeries);
      }
      
      // 顾客人数系列：每个年份的12个月份堆叠
      for(int month = 1; month <= 12; month++) {
        String monthName = month + "月_顾客";
        List<Integer> monthDataList = new ArrayList<>();
        for(String year : years) {
          List<IncomeInterface> monthlyIncomes = statIncomes("%Y-%m", year + "-01", -1);
          int monthValue = 0;
          for(IncomeInterface mi : monthlyIncomes) {
            String miDate = mi.getDate();
            String miYearStr = miDate.substring(0, 4);
            String miMonthStr = miDate.substring(miDate.length() - 2);
            int miYear = Integer.parseInt(miYearStr);
            int miMonth = Integer.parseInt(miMonthStr);
            if(miYear == Integer.parseInt(year) && miMonth == month) {
              monthValue = mi.getPopulation();
              break;
            }
          }
          monthDataList.add(monthValue);
        }
        EChartsSeries populationSeries = new EChartsSeries(monthName, monthDataList);
        populationSeries.setyAxisIndex(1);
        populationSeries.setStack("populationStack");
        populationSeries.setType("bar");
        populationSeries.setBarGap("0%");
        populationSeries.setBarWidth("40%");
        seriesList.add(populationSeries);
      }
      
      // 图例只显示"收入"和"顾客人数"
      legends.add("收入");
      legends.add("顾客人数");
      
      // 计算每个年份的合计值
      Map<String, Integer> yearIncomeTotals = new HashMap<>();
      Map<String, Integer> yearPopulationTotals = new HashMap<>();
      for(String year : years) {
        yearIncomeTotals.put(year, 0);
        yearPopulationTotals.put(year, 0);
      }
      
      // 计算每个年份的收入和顾客人数合计
      for(EChartsSeries series : seriesList) {
        String seriesName = series.getName();
        boolean isIncome = seriesName.contains("收入");
        boolean isPopulation = seriesName.contains("顾客");
        
        for(int i = 0; i < series.getData().length; i++) {
          String year = years.get(i);
          Integer value = series.getData()[i];
          if(value != null) {
            if(isIncome) {
              yearIncomeTotals.put(year, yearIncomeTotals.get(year) + value);
            }
            if(isPopulation) {
              yearPopulationTotals.put(year, yearPopulationTotals.get(year) + value);
            }
          }
        }
      }
      
      // 添加收入合计series
      List<Integer> incomeTotalData = new ArrayList<>();
      for(String year : years) {
        incomeTotalData.add(yearIncomeTotals.get(year));
      }
      EChartsSeries incomeTotalSeries = new EChartsSeries("收入合计", incomeTotalData);
      incomeTotalSeries.setyAxisIndex(0);
      incomeTotalSeries.setStack("incomeStack");
      incomeTotalSeries.setType("bar");
      incomeTotalSeries.setBarGap("0%");
      incomeTotalSeries.setBarWidth("40%");
      
      EChartsSeries.Label incomeLabel = new EChartsSeries.Label();
      incomeLabel.setShow(true);
      incomeLabel.setPosition("top");
      incomeLabel.setFontSize(14);
      incomeLabel.setFontWeight("bold");
      incomeTotalSeries.setLabel(incomeLabel);
      
      EChartsSeries.ItemStyle incomeItemStyle = new EChartsSeries.ItemStyle();
      incomeItemStyle.setOpacity(0.0);
      incomeTotalSeries.setItemStyle(incomeItemStyle);
      
      seriesList.add(incomeTotalSeries);
      
      // 添加顾客合计series
      List<Integer> populationTotalData = new ArrayList<>();
      for(String year : years) {
        populationTotalData.add(yearPopulationTotals.get(year));
      }
      EChartsSeries populationTotalSeries = new EChartsSeries("顾客合计", populationTotalData);
      populationTotalSeries.setyAxisIndex(1);
      populationTotalSeries.setStack("populationStack");
      populationTotalSeries.setType("bar");
      populationTotalSeries.setBarGap("0%");
      populationTotalSeries.setBarWidth("40%");
      
      EChartsSeries.Label populationLabel = new EChartsSeries.Label();
      populationLabel.setShow(true);
      populationLabel.setPosition("top");
      populationLabel.setFontSize(14);
      populationLabel.setFontWeight("bold");
      populationTotalSeries.setLabel(populationLabel);
      
      EChartsSeries.ItemStyle populationItemStyle = new EChartsSeries.ItemStyle();
      populationItemStyle.setOpacity(0.0);
      populationTotalSeries.setItemStyle(populationItemStyle);
      
      seriesList.add(populationTotalSeries);
    } else {
      // 按天或按月统计
      for (int i = 0; i < incomes.size(); i++) {
        IncomeInterface income = incomes.get(i);
        String date_x = income.getDate();
        int income_y1 = income.getIncome();
        int population_y2 = income.getPopulation();
        if (i == 0) {
          // 首次迭代: 直接放入xDataList
          xDataList.add(date_x);
          // 首次迭代: 直接放入seriesMap
          List<Integer> income_y1_list = new ArrayList<>();
          income_y1_list.add(income_y1);
          seriesMap.put("收入", income_y1_list);
          List<Integer> population_y2_list = new ArrayList<>();
          population_y2_list.add(population_y2);
          seriesMap.put("顾客人数", population_y2_list);
        } else {
          // 多次迭代: 补全x轴间断日期及其对应y轴数据
          List<String> gapDates = getGapDates(xDataList.get(xDataList.size() - 1), date_x, dateFormat4java);
          // 补全x轴间断日期
          xDataList.addAll(gapDates);
          // 更新seriesMap中老的系列值
          List<Integer> income_y1_list = seriesMap.get("收入");
          List<Integer> population_y2_list = seriesMap.get("顾客人数");
          // 补全对应y轴数据
          for(int y=0;y<gapDates.size();y++){
            income_y1_list.add(0);
            population_y2_list.add(0);
          }
          // 最后加上本次迭代数据
          xDataList.add(date_x);
          income_y1_list.add(income_y1);
          population_y2_list.add(population_y2);
          seriesMap.put("收入", income_y1_list);
          seriesMap.put("顾客人数", population_y2_list);
        }
      }
      for (Map.Entry<String, List<Integer>> entry : seriesMap.entrySet()) {
        EChartsSeries series1 = new EChartsSeries(entry.getKey(), entry.getValue());
        if(series1.getName().equals("收入")){
            series1.setyAxisIndex(0);
          }else if(series1.getName().equals("顾客人数")){
            series1.setyAxisIndex(1);
            series1.setType("bar");
          }
        seriesList.add(series1);
      }
    }
    
    EChartsOption eChartsOption = new EChartsOption(title, legends, xDataList, seriesList, yAxisList);
    eChartsOption.setTooltipStack(true);
    eChartsOption.setDurPeriod(durPeriod);
    LOG.info("eChartsOption={}", eChartsOption);
    JSONObject jsonObj = JSONObject.parseObject(eChartsOption.toString());
    LOG.info("jsonObj={}", jsonObj);
    return jsonObj;
  }

  /**
   * 获取两个日期之间的跨度
   * @param beginDate 开始日期
   * @param endDate 结束日期
   * @param dateFormat 日期格式, 目前支持日(%Y-%m-%d), 月(%Y-%m), 年(%Y)
   * @return 跨度的日期列表, 没有跨度则返回空列表
   */
  private List<String> getGapDates(String beginDate, String endDate, String dateFormat) {
    List<String> gapDates = new ArrayList<>();
    Calendar beginCalendar = Calendar.getInstance();
    Calendar endCalendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    try {
      Date bd = sdf.parse(beginDate);
      Date ed = sdf.parse(endDate);
      beginCalendar.setTime(bd);
      endCalendar.setTime(ed);
    } catch (ParseException e) {
      LOG.error(e.toString(), e);
      e.printStackTrace();
    }
    // 日期跨度
    long minus = 1;
    switch (dateFormat) {
      case "yyyy-MM-dd":
        long ms = endCalendar.getTimeInMillis() - beginCalendar.getTimeInMillis();
        // 相差天数
        long days = ms / 1000 / 3600 / 24;
        minus = days;
        break;
      case "yyyy-MM":
        int minusMonth = endCalendar.get(Calendar.MONTH) - beginCalendar.get(Calendar.MONTH);
        int minusYear = endCalendar.get(Calendar.YEAR) - beginCalendar.get(Calendar.YEAR);
        // 相差几个月
        int months = minusMonth + minusYear * 12;
        minus = months;
        break;
      case "yyyy":
        // 相差几年
        int years = endCalendar.get(Calendar.YEAR) - beginCalendar.get(Calendar.YEAR);
        minus = years;
        break;
      default:
        LOG.error("不合法的dateFormat:{}, 不做任何处理!", dateFormat);
    }
    if(minus != 1){
      for (;minus > 1;minus--){
        beginCalendar.add(Calendar.DATE, 1);
        gapDates.add(sdf.format(beginCalendar.getTime()));
      }
    }
    return gapDates;
  }

  @RequestMapping("/filter/stats/customerHome")
  public String customerHome() {
    return "filter/stats/customerChart";
  }

  /**
   * 在使用 @RequestMapping后，返回值通常解析为跳转路径，
   * 但是加上 @ResponseBody 后返回结果不会被解析为跳转路径，而是直接写入 HTTP response body 中。
   * 比如异步获取 json 数据，加上 @ResponseBody 后，会直接返回 json 数据。
   *
   * @param durPeriod 统计周期: 天1/月2/年3
   * @return 统计结果数据
   */
  @RequestMapping("/filter/stats/customer")
  @ResponseBody
  public JSONObject customerChart(Integer durPeriod, Integer durVal) {
    LocalDate startDate = LocalDate.now().minusDays(durVal);
    int startYear = startDate.getYear();
    int startMonth = startDate.getMonthValue();
    List<CustomerInterface> customers = new ArrayList<>();
    // 第三个返回值为java的SimpleDateFormat格式
    String[] args = formatByDur(durPeriod, durVal);
    String dateFormat4sql = args[0];
    String startDay = args[1];
    String dateFormat4java = args[2];
    customers = durVal <= 0 ?
            statService.statCustomerByDateFormat(dateFormat4sql)
            : statService.statCustomerByDateFormatAndStartDay(dateFormat4sql, startDay);
    String title = "客户统计";
    List<String> legends = new ArrayList<>();
    List<String> xDataList = new ArrayList<>();
    List<ECharts_yAxis> yAxisList = new ArrayList<>();
    yAxisList.add(new ECharts_yAxis("顾客量","left","人次"));
    List<EChartsSeries> seriesList = new ArrayList<>();
    Map<String, List<Integer>> seriesMap = new HashMap<>();
    for (int i=0;i<customers.size();i++) {
      CustomerInterface customer = customers.get(i);
      String legend = StaticContrast.parseRole(customer.getRole());
      String date_x = customer.getDate();
      int value_y = customer.getTotal();
      if (i == 0) {
        // 首次迭代: 直接放入legends
        legends.add(legend);
        // 首次迭代: 直接放入xDataList
        xDataList.add(date_x);
        // 首次迭代: 直接放入seriesMap
        List<Integer> values = new ArrayList<>();
        values.add(value_y);
        seriesMap.put(legend, values);
      } else {
        if (!legends.contains(legend)) {
          // 去重放入xDataList
          if(!xDataList.contains(date_x)) xDataList.add(date_x);
          // 多次迭代同一日期(x)不同角色(系列)
          legends.add(legend);
          // 不同角色(系列): 直接放入seriesList
          List<Integer> values = new ArrayList<>();
          values.add(value_y);
          seriesMap.put(legend, values);
        } else {
          if (legend.equals(legends.get(0))) {
            // 多次迭代不同日期(x): 补全x轴间断日期及其对应y轴数据
            List<String> gapDates = getGapDates(xDataList.get(xDataList.size() - 1), date_x, dateFormat4java);
            // 补全x轴间断日期
            xDataList.addAll(gapDates);
            for(String gapDate : gapDates) {
              for (String legendTemp : legends) {
                // 更新seriesMap中老的系列值: 所有角色(系列)都要更新
                List<Integer> oldValues = seriesMap.get(legendTemp);
                // 补全对应y轴数据
                oldValues.add(0);
                seriesMap.put(legendTemp, oldValues);
              }
            }
          }
          // 最后加上本次迭代数据
          // 去重放入xDataList
          if(!xDataList.contains(date_x)) xDataList.add(date_x);
          // 更新seriesMap中老的系列值: 所有角色(系列)都要更新
          List<Integer> oldValues = seriesMap.get(legend);
          // 同一个系列: 更新seriesList中老的系列值
          oldValues.add(value_y);
          seriesMap.put(legend, oldValues);
        }
      }
    }
    // 正确处理排序：保存原始顺序，排序后重新排列series数据
    List<String> originalXList = new ArrayList<>(xDataList);
    Collections.sort(xDataList);
    
    // 重新排列所有series的数据，使其与排序后的xDataList对应
    Map<String, List<Integer>> newSeriesMap = new HashMap<>();
    for (Map.Entry<String, List<Integer>> entry : seriesMap.entrySet()) {
      List<Integer> newValues = new ArrayList<>();
      List<Integer> oldValues = entry.getValue();
      for (String xVal : xDataList) {
        int originalIndex = originalXList.indexOf(xVal);
        if (originalIndex >= 0 && originalIndex < oldValues.size()) {
          newValues.add(oldValues.get(originalIndex));
        } else {
          newValues.add(0);
        }
      }
      newSeriesMap.put(entry.getKey(), newValues);
    }
    seriesMap = newSeriesMap;
    
    for (Map.Entry<String, List<Integer>> entry : seriesMap.entrySet()) {
      EChartsSeries series1 = new EChartsSeries(entry.getKey(), entry.getValue());
      seriesList.add(series1);
    }
    EChartsOption eChartsOption = new EChartsOption(title, legends, xDataList, seriesList, yAxisList);
    eChartsOption.setDurPeriod(durPeriod);
    LOG.info("eChartsOption={}", eChartsOption);
    JSONObject jsonObj = JSONObject.parseObject(eChartsOption.toString());
    LOG.info("jsonObj={}", jsonObj);
    return jsonObj;
  }
  /*
  @RequestMapping("/filter/stats/customer")
  @ResponseBody
  public Option customerChart(Integer dur,Model model){
    List<Customer> customers = new ArrayList<>();
    switch (dur) {
      case 1:
        customers = statService.customer4month();
        break;
      case 2:
        customers = statService.customer4month();
        break;
      default:
        LOG.info("不合法的统计时间区间:{}",dur);
    }
    String title = "客户统计";
    List<String> legends = new ArrayList<>();
    List<String> dates = new ArrayList<>();
    List<Series> series = new ArrayList<>();
    MarkPoint mp = new MarkPoint();
    mp.data(new Data().type(MarkType.max).name("最大值"),
            new Data().type(MarkType.min).name("最小值"));
    for(int i=0;i<customers.size();i++) {
      String legend = customers.get(i).parseRole();
      String date_x = customers.get(i).getDate();
      int value_y = customers.get(i).getTotal();
      if(!legends.contains(legend)) legends.add(legend);
      if(!dates.contains(date_x)) dates.add(date_x);
      Line line = new Line();
      line.name(legend).type(SeriesType.line).data(value_y).markPoint(mp);
      series.add(line);
    }
    Option option = new GsonOption();
    // 设置标题
    option.title().text(title);
    option.tooltip().trigger(Trigger.axis);
    // 设置图例(折线)
    option.legend().data(legends);
    ValueAxis axis = new ValueAxis();
    axis.type(AxisType.category).boundaryGap(false).data(dates);
    // 设置X轴
    option.xAxis(axis);
    CategoryAxis yaxis = new CategoryAxis();
    yaxis.type(AxisType.value);
    // 设置Y轴
    option.yAxis(yaxis);
    // 设置
    option.series(series);
    return option;
//    jsonObj=JSONObject.fromObject(option.toString());
//    LOG.info("option={}",option);
//    model.addAttribute("option",option);
//    return "filter/stats/customerChart";
  }
  */
}
