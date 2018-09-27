package tspGA;

import java.lang.reflect.Array;
import java.util.Random;

public class TspGA_lib {

	int sedai_max = 2000;// 世代数
	int cros_rate = 90;// 交差率
	int muta_rate = 5;// 突然変異率
	int weight_max = 70;// 重量制限
	int city_num = 20;// 都市数
	int kotai_num;

	public TspGA_lib(int kotai_num, int sedai_max, int cros_rate, int muta_rate, int weight_max, int city_num) {
		this.kotai_num = kotai_num;
		this.sedai_max = sedai_max;
		this.cros_rate = cros_rate;
		this.muta_rate = muta_rate;
		this.weight_max = weight_max;
		this.city_num = city_num;

		int[][] city = latlon();// 都市の座標を発生
		int[][] gene = create_gane();// 初期個体の発生
		for(int i = 0; i< this.sedai_max; i++) {
			shinka(city, gene);// 進化計算
		}
	}

	private void shinka(int[][] city, int[][] gene) {
		// TODO Auto-generated method stub
		double[] score = shinka_score(city, gene); // 採点
		shinla_min(score);// 最低得点個体の抽出
		int[][] next_gene = shinka_roolet(score, gene);// ルーレット選択
		shinka_swich(next_gene);// 一点交差

	}

	private void shinka_swich(int[][] next_gene) {
		// TODO Auto-generated method stub
		// 一点交差の位置を決める
		int cross_num = kotai_num / 2;
		for (int i = 0; i < cross_num; i++) {
			Random rand = new Random();
			int c_pos = rand.nextInt(city_num);
			int[] taihi = new int[city_num];
			for (int j = 0; j < city_num - c_pos; j++) {
				taihi[i] = next_gene[i * 2][j + c_pos];
			}
			int betu_num = 0;
			int[] betu = new int[city_num];
			for (int j = 0; j < city_num; j++) {
				for (int k = 0; k < city_num - c_pos; k++) {
					if (next_gene[i * 2 + 1][j] == taihi[k]) {
						betu[betu_num] = next_gene[i * 2][j];
						betu_num++;
					}
				}
			}
			for (int j = 0; j < city_num - c_pos; j++) {
				next_gene[i * 2][j + c_pos] = betu[j];
			}
			
			
			for(int j = 0; j< city_num-c_pos;j++) {
				taihi[j]=next_gene[j*2][j+c_pos];
			}
			//betu_num = 0;
			int[] betu2 = new int[city_num];
			for (int j = 0; j < city_num/2; j++) {
				for (int k = 0; k < city_num - c_pos; k++) {
					if (next_gene[i * 2 + 2][j] == taihi[k]) {
					betu2[betu_num] = next_gene[i * 2 ][j];
					betu_num++;
					}
				}
			}
		}

	}

	private int[][] shinka_roolet(double[] score, int[][] gene) {
		// TODO Auto-generated method stub
		int max = 0;// 最長経路長を求める
		for (int i = 0; i < kotai_num - 1; i++) {
			if (score[max] < score[i + 1]) {
				max = i + 1;
			}
		}
		// 最長経路から自分の経路長を引いたのを得点
		double[] score2 = new double[kotai_num];// 経路長が短いと得点が高くなるようにする
		for (int i = 0; i < kotai_num; i++) {
			score2[i] = score[max] - score[i];
		}
		int total = 0;
		for (int i = 0; i < kotai_num; i++) {
			total += score2[i];
		}

		// 個体数分のルーレット選択を行う
		int ya_kotai = 0;
		int[][] next_gene = null;
		for (int i = 0; i < kotai_num; i++) {
			Random rand = new Random();
			int ya = rand.nextInt(total);// 矢を放つ
			int tumiage = 0;// 矢が当たったのは何番目の個体であるか？
			for (int j = 0; j < kotai_num; j++) {
				tumiage += score[j];
				if (tumiage > ya) {
					ya_kotai = j;
					break;
				}
			}
			for (int j = 0; j < kotai_num; j++) {
				next_gene[i][j] = gene[ya_kotai][j];
			}
		}
		return next_gene;
	}

	private void shinla_min(double[] score) {
		// TODO Auto-generated method stub
		// 最低個体の抽出
		int min = 0;
		for (int i = 0; i < kotai_num - 1; i++) {
			if (score[min] > score[i + 1]) {
				min = i + 1;
			}
		}
	}

	private double[] shinka_score(int[][] city, int[][] gene) {
		// TODO Auto-generated method stub
		// 個体群の採点
		double[] score = new double[city_num];
		for (int i = 0; i < city_num; i++) {
			int x_sa;
			int y_sa;
			int temp;
			for (int j = 0; j < city_num - 1; j++) {
				x_sa = city[0][gene[i][j]] - city[0][gene[i][j + 1]];
				y_sa = city[1][gene[i][j]] - city[1][gene[i][j + 1]];
				temp = x_sa * x_sa + y_sa * y_sa;
				score[i] += Math.sqrt((double) temp);
			}
		}
		return score;

	}

	private int[][] create_gane() {
		// TODO Auto-generated method stub
		// 初期個体群の生成
		int[][] gene = new int[city_num][city_num];
		for (int i = 0; i < city_num; i++) {
			for (int j = 0; j < city_num; j++) {
				gene[i][j] = j;
			}
			// シャッフル
			Random rand = new Random();
			int temp1;
			int temp2;
			int temp;
			for (int j = 0; j < city_num; j++) {
				temp1 = rand.nextInt(city_num);
				temp2 = rand.nextInt(city_num);
				temp = gene[i][temp1];
				gene[i][temp1] = gene[i][temp2];
				gene[i][temp2] = temp;
			}
		}
		return gene;
	}

	private int[][] latlon() {
		// TODO Auto-generated method stub
		int[][] city = new int[2][city_num];
		Random rand = new Random();
		for (int i = 0; i < city_num; i++) {
			city[0][i] = rand.nextInt(600) + 100;
			city[1][i] = rand.nextInt(550) + 50;
		}
		return city;
	}

}
