package translators;

import java.util.ArrayList;
import java.util.List;

public class DataBitDecoder {

	public List<List<String>> dataConfig8Bit() {
		List<List<String>> data8Bit = new ArrayList<>();
		List<String> dataInterSeg8Bit = new ArrayList<>();
		List<String> dataSeg8Bit = new ArrayList<>();
		
		dataSeg8Bit.add("0|0|125");
		dataSeg8Bit.add("1|125|250");
		dataSeg8Bit.add("2|250|375");
		dataSeg8Bit.add("3|375|500");
		dataSeg8Bit.add("4|500|625");
		dataSeg8Bit.add("5|625|750");
		dataSeg8Bit.add("6|750|875");
		dataSeg8Bit.add("7|875|1000");
		
		dataInterSeg8Bit.add("|0|0|7,8125");
		dataInterSeg8Bit.add("|1|7,8125|15,625");
		dataInterSeg8Bit.add("|2|15,625|23,4375");
		dataInterSeg8Bit.add("|3|23,4375|31,25");
		dataInterSeg8Bit.add("|4|31,25|39,0625");
		dataInterSeg8Bit.add("|5|39,0625|46,875");
		dataInterSeg8Bit.add("|6|46,875|54,6875");
		dataInterSeg8Bit.add("|7|54,6875|62,5");
		dataInterSeg8Bit.add("|8|62,5|70,3125");
		dataInterSeg8Bit.add("|9|70,3125|78,125");
		dataInterSeg8Bit.add("|10|78,125|85,9375");
		dataInterSeg8Bit.add("|11|85,9375|93,75");
		dataInterSeg8Bit.add("|12|93,75|101,5625");
		dataInterSeg8Bit.add("|13|101,5625|109,375");
		dataInterSeg8Bit.add("|14|109,375|117,1875");
		dataInterSeg8Bit.add("|15|117,1875|125");
		
		data8Bit.add(0,dataSeg8Bit);
		data8Bit.add(1,dataInterSeg8Bit);
		return data8Bit;
	}

	public List<List<String>> dataConfig9Bit() {
		List<List<String>> data9Bit = new ArrayList<>();
		List<String> dataInterSeg9Bit = new ArrayList<>();
		List<String> dataSeg9Bit = new ArrayList<>();
		
		dataSeg9Bit.add("0|0|62,5");
		dataSeg9Bit.add("1|62,5|125");
		dataSeg9Bit.add("2|125|187,5");
		dataSeg9Bit.add("3|187,5|250");
		dataSeg9Bit.add("4|250|312,5");
		dataSeg9Bit.add("5|312,5|375");
		dataSeg9Bit.add("6|375|437,5");
		dataSeg9Bit.add("7|437,5|500");
		dataSeg9Bit.add("8|500|562,5");
		dataSeg9Bit.add("9|562,5|625");
		dataSeg9Bit.add("10|625|687,5");
		dataSeg9Bit.add("11|687,5|750");
		dataSeg9Bit.add("12|750|812,5");
		dataSeg9Bit.add("13|812,5|875");
		dataSeg9Bit.add("14|875|937,5");
		dataSeg9Bit.add("15|937,5|1000");

		dataInterSeg9Bit.add("0|0|3,9062");
		dataInterSeg9Bit.add("1|3,9062|7,8125");
		dataInterSeg9Bit.add("2|7,8125|11,7188");
		dataInterSeg9Bit.add("3|11,7188|15,625");
		dataInterSeg9Bit.add("4|15,625|19,5312");
		dataInterSeg9Bit.add("5|19,5312|23,4375");
		dataInterSeg9Bit.add("6|23,4375|27,3438");
		dataInterSeg9Bit.add("7|27,3438|31,25");
		dataInterSeg9Bit.add("8|31,25|35,1562");
		dataInterSeg9Bit.add("9|35,1562|39,0625");
		dataInterSeg9Bit.add("10|39,0625|42,9688");
		dataInterSeg9Bit.add("11|42,9688|46,875");
		dataInterSeg9Bit.add("12|46,875|50,7812");
		dataInterSeg9Bit.add("13|50,7812|54,6875");
		dataInterSeg9Bit.add("14|54,6875|58,5938");
		dataInterSeg9Bit.add("15|58,5938|62,5");

		data9Bit.add(0,dataSeg9Bit);
		data9Bit.add(1,dataInterSeg9Bit);
		return data9Bit;
	}

	public List<String> dataConfig10Bit() {
		List<String> data10Bit = new ArrayList<>();

		return null;
	}

}
