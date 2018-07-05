package cn.com.infohold.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.abel533.echarts.Option;
import com.github.abel533.echarts.Tooltip;
import com.github.abel533.echarts.axis.AxisLabel;
import com.github.abel533.echarts.axis.CategoryAxis;
import com.github.abel533.echarts.axis.ValueAxis;
import com.github.abel533.echarts.code.Trigger;
import com.github.abel533.echarts.data.PieData;
import com.github.abel533.echarts.series.Bar;
import com.github.abel533.echarts.series.Line;
import com.github.abel533.echarts.series.Pie;
import com.github.abel533.echarts.series.Series;
import com.github.abel533.echarts.style.TextStyle;

import cn.com.infohold.basic.util.jdbc.BasicJdbcUtil;
import cn.com.infohold.basic.util.jdbc.JdbcConBean;
import cn.com.infohold.basic.util.json.BasicJsonUtil;

public class EChartUtil {

	public static Option getDefaultOption() {
		Option option = new Option();
		Tooltip tooltip = new Tooltip();
		tooltip.setTrigger(Trigger.axis);
		tooltip.setTextStyle(new TextStyle().fontSize(8));
		tooltip.setPadding(10);
		option.tooltip(tooltip);
		return option;
	}

	public static Option columnar(Map<String, Object> data) {
		Option option = getDefaultOption();
		List<Series> series = new ArrayList<Series>();
		Object[] xAxis = (Object[]) data.get("xAxis");
		Object[] legend = (Object[]) data.get("legend");
		Map<String, Object> dataMap = (Map<String, Object>) data.get("series");
		System.out.println(dataMap.toString());
		for (String k1 : dataMap.keySet()) {
			List<Object> objects = (List<Object>) dataMap.get(k1);
			Bar bar = new Bar();
			bar.data(objects.toArray()).name(k1.trim());
			series.add(bar);
		}
		option.xAxis(
				new CategoryAxis().data(xAxis).boundaryGap(true).axisLabel(new AxisLabel().interval(0).rotate(-30)))
				.yAxis(new ValueAxis());
		option.legend(legend).legend().setX("right");
		option.series(series);
		return option;
	}

	public static Option barType(Map<String, Object> data) {
		Option option = getDefaultOption();
		List<Series> series = new ArrayList<Series>();
		Object[] xAxis = (Object[]) data.get("xAxis");
		Object[] legend = (Object[]) data.get("legend");
		Map<String, Object> dataMap = (Map<String, Object>) data.get("series");
		System.out.println(dataMap.toString());
		for (String k1 : dataMap.keySet()) {
			List<Object> objects = (List<Object>) dataMap.get(k1);
			Bar bar = new Bar();
			bar.data(objects.toArray()).name(k1.trim());
			series.add(bar);
		}
		option.yAxis(
				new CategoryAxis().data(xAxis).boundaryGap(true).axisLabel(new AxisLabel().interval(0).rotate(-30)))
				.xAxis(new ValueAxis());
		option.legend(legend).legend().setX("right");
		option.series(series);
		return option;
	}

	public static Option line(Map<String, Object> data) {
		Option option = getDefaultOption();
		List<Series> series = new ArrayList<Series>();
		Object[] xAxis = (Object[]) data.get("xAxis");
		Object[] legend = (Object[]) data.get("legend");
		Map<String, Object> dataMap = (Map<String, Object>) data.get("series");

		System.out.println(dataMap.toString());
		for (String k1 : dataMap.keySet()) {
			List<Object> objects = (List<Object>) dataMap.get(k1);
			Line line = new Line().smooth(true);
			line.data(objects.toArray()).name(k1.trim());
			series.add(line);
		}
		option.xAxis(
				new CategoryAxis().data(xAxis).boundaryGap(true).axisLabel(new AxisLabel().interval(0).rotate(-30)))
				.yAxis(new ValueAxis());
		option.legend(legend).legend().setX("right");
		option.series(series);
		return option;

	}

	public static Option area(Map<String, Object> data) {
		Option option = getDefaultOption();
		List<Series> series = new ArrayList<Series>();

		Object[] xAxis = (Object[]) data.get("xAxis");
		Object[] legend = (Object[]) data.get("legend");
		Map<String, Object> dataMap = (Map<String, Object>) data.get("series");

		System.out.println(dataMap.toString());
		for (String k1 : dataMap.keySet()) {
			List<Object> objects = (List<Object>) dataMap.get(k1);
			Line line = new Line().smooth(true);
			line.data(objects.toArray()).name(k1.trim()).itemStyle().normal().areaStyle().typeDefault();
			series.add(line);
		}
		option.xAxis(
				new CategoryAxis().data(xAxis).boundaryGap(true).axisLabel(new AxisLabel().interval(0).rotate(-30)))
				.yAxis(new ValueAxis());
		option.legend(legend).legend().setX("right");
		option.series(series);
		return option;

	}

	public static Option pieChart(Map<String, Object> data) {
		Option option = getDefaultOption();
		List<Series> series = new ArrayList<Series>();

		Object[] xAxis = (Object[]) data.get("xAxis");
		Object[] legend = (Object[]) data.get("legend");
		Map<String, Object> dataMap = (Map<String, Object>) data.get("series");

		System.out.println(dataMap.toString());
		for (String k1 : dataMap.keySet()) {
			List<Object> objects = (List<Object>) dataMap.get(k1);
			Pie pie = new Pie();
			pie.data(new PieData(k1.toString(), objects)).name(k1.trim());
			series.add(pie);
		}
		option.xAxis(
				new CategoryAxis().data(xAxis).boundaryGap(true).axisLabel(new AxisLabel().interval(0).rotate(-30)))
				.yAxis(new ValueAxis());
		option.legend(legend).legend().setX("right");
		option.series(series);
		return option;

	}

	public static void main(String[] args) throws Exception {
		JdbcConBean jdbcConBean = new JdbcConBean();
		jdbcConBean.setJdbcDriver("com.mysql.jdbc.Driver");
		jdbcConBean.setJdbcPassword("lifanhong");
		jdbcConBean.setJdbcUserName("root");
		jdbcConBean.setJdbcURL("jdbc:mysql://192.168.31.72:3306/bdp_basic");
		List<Map<String, Object>> list = BasicJdbcUtil.getInstance().select(jdbcConBean,
				"select process_id,business_exec_status,count(1) ccc from sch_business_transactions group by process_id,business_exec_status");

		Map<String, Object> echartMap = convertEchartMap(list,"process_id","business_exec_status");

		System.out.println(BasicJsonUtil.getInstance().toJsonString(line(echartMap)));
	}

	private static Map<String, Object> convertEchartMap(List<Map<String, Object>> list,String groupStr,String contrastStr) {
		Map<String, Object> echartMap = new HashMap<String, Object>();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		for (Map<String, Object> map : list) {
			String key = String.valueOf(map.get(groupStr));
			String key1 = String.valueOf(map.get(contrastStr));
			String v = String.valueOf(map.get("ccc"));
			if (resultMap.containsKey(key)) {
				Map<String, Object> temp = (Map<String, Object>) resultMap.get(key);
				if (temp.containsKey(key1)) {
					List<Object> tempList = (List<Object>) temp.get(key1);
					tempList.add(v);
					temp.put(key1, tempList);
				} else {
					List<Object> tempList = new ArrayList<Object>();
					tempList.add(v);
					temp.put(key1, tempList);
				}
				resultMap.put(key, temp);
			} else {
				Map<String, Object> temp = new HashMap<String, Object>();
				for (String k1 : map.keySet()) {
					List<Object> tempList = new ArrayList<Object>();
					tempList.add(v);
					temp.put(key1, tempList);
				}
				resultMap.put(key, temp);
			}
			System.out.println(map.toString());
		}
		Map<String, Object> dataMap = new HashMap<String, Object>();
		for (String key : resultMap.keySet()) {
			Map<String, Object> map = (Map<String, Object>) resultMap.get(key);
			for (String k1 : map.keySet()) {
				if (dataMap.containsKey(k1)) {
					List<Object> objects = (List<Object>) dataMap.get(k1);
					List<Object> objects1 = (List<Object>) map.get(k1);
					objects.addAll(objects1);
					dataMap.put(k1, objects);
				} else {
					List<Object> objects1 = (List<Object>) map.get(k1);
					dataMap.put(k1, objects1);
				}
			}
		}
		echartMap.put("xAxis", resultMap.keySet().toArray());
		echartMap.put("legend", dataMap.keySet().toArray());
		echartMap.put("series", dataMap);
		return echartMap;
	}

}
