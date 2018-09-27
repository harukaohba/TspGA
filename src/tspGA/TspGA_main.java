package tspGA;

public class TspGA_main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		int kotai_num = 2000;// 個体数
		int sedai_max = 2000;// 世代数
		int cros_rate = 90;// 交差率
		int muta_rate = 5;// 突然変異率
		int weight_max = 70;// 重量制限
		int city_num = 20;//都市数
		
		TspGA_lib tlib = new TspGA_lib(kotai_num, sedai_max, cros_rate, muta_rate, weight_max, city_num);
		
	}

}
