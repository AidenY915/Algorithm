import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class MyComparator implements Comparator<Integer> {

	public MyComparator(List<List<Integer>> abilities, int index) {
		this.abilities = abilities;
		this.index = index;
	}

	private List<List<Integer>> abilities;
	private int index;

	@Override
	public int compare(Integer o1, Integer o2) {
		int o1MaxIndex = abilities.get(o1).get(3);
		int o2MaxIndex = abilities.get(o2).get(3);
		return -(abilities.get(o1).get(index) - abilities.get(o1).get(o1MaxIndex))
				+ (abilities.get(o2).get(index) - abilities.get(o2).get(o2MaxIndex));
	}

}

public class Q2_2 {

	private static int getSumOfAbilities(List<List<Integer>> abilities) {
		int sum = 0;
		int size = abilities.size();
		for (int i = 0; i < size; i++) {
			sum += abilities.get(i).get(0) + abilities.get(i).get(1) + abilities.get(i).get(2);
		}
		return sum;
	}

	private static List<List<Integer>> get2dList(int n) {
		List<List<Integer>> list2d = new ArrayList<>(3);
		for (int i = 0; i < 3; i++) {
			list2d.add(new ArrayList<Integer>(n));
		}
		return list2d;
	}

	private static List<List<Integer>> initAbilities(int n) {
		List<List<Integer>> abilities = new ArrayList<>(n);
		for (int i = 0; i < n; i++) {
			abilities.add(new ArrayList<Integer>(4));
		}
		return abilities;
	}

	private static int getMaxAbIndex(List<Integer> ability, int[] theOther) {
		int ab1 = ability.get(0), ab2 = ability.get(1), ab3 = ability.get(2);
		int max = Math.max(ab1, Math.max(ab2, ab3));
		if (max == ab1) {
			theOther[0] = 1;
			theOther[1] = 2;
			return 0;
		}
		if (max == ab2) {
			theOther[0] = 0;
			theOther[1] = 2;
			return 1;
		}
		theOther[0] = 0;
		theOther[1] = 1;
		return 2;
	}

	private static int[] initailizeGalaxy(List<List<Integer>> abilities, List<List<Integer>> list2d) {
		int[] galaxy = new int[3];
		int size = abilities.size();
		int[] theOther = new int[2];
		for (int i = 0; i < size; i++) {
			int maxIndex = getMaxAbIndex(abilities.get(i), theOther);
			abilities.get(i).add(maxIndex); // 네번째 원소에 maxIndex 저장
			galaxy[maxIndex] += abilities.get(i).get(maxIndex);
			if (abilities.get(i).get(theOther[0]) != 0) {
				list2d.get(theOther[0]).add(i);
			}
			if (abilities.get(i).get(theOther[1]) != 0) {
				list2d.get(theOther[1]).add(i);
			}
		}
		return galaxy;
	}

	private static void sortList2d(List<List<Integer>> list2d, List<List<Integer>> abilities) {
		for (int i = 0; i < 3; i++) {
			List<Integer> list = list2d.get(i);
			Collections.sort(list, new MyComparator(abilities, i));
			//System.out.println(list);
		}
	}

	private static int getSumOfGalaxy(int[] galaxy) {
		return galaxy[0] + galaxy[1] + galaxy[2];
	}

	private static boolean isValidGalaxy(int[] galaxy) {
		return (galaxy[0] > 
		0 && galaxy[1] > 0 && galaxy[2] > 0);
	}

	private static int[] modifyGalaxy(int[] galaxy, List<List<Integer>> abilities, List<List<Integer>> list2d) {
		if (galaxy[0] + galaxy[1] + galaxy[2] < 3)
			return null;
		int[] theOther = new int[] { -1, -1 };

		for (int i = 0; i < 3; i++) {
			if (galaxy[i] <= 0) {
				if (theOther[0] == -1)
					theOther[0] = i;
				else
					theOther[1] = i;
			}
		}
		if (isValidGalaxy(galaxy))
			return galaxy;
		int[] maxGalaxy = new int[] { 0, 0, 0 };

		for (int i = 0; i < list2d.get(theOther[0]).size() && i < 2; i++) {
			int[] tmpGalaxy = new int[] { galaxy[0], galaxy[1], galaxy[2] };
			int agentIdx0 = list2d.get(theOther[0]).get(i);
			int maxIdx0 = abilities.get(agentIdx0).get(3);
			tmpGalaxy[maxIdx0] -= abilities.get(agentIdx0).get(maxIdx0);
			tmpGalaxy[theOther[0]] += abilities.get(agentIdx0).get(theOther[0]);
			if (theOther[1] != -1) {
				for (int j = 0; j < list2d.get(theOther[1]).size() && j < 2; j++) {
					if(list2d.get(theOther[0]).get(i) == list2d.get(theOther[1]).get(j))
						continue;
					int agentIdx1 = list2d.get(theOther[1]).get(j);
					int maxIdx1 = abilities.get(agentIdx1).get(3);
					tmpGalaxy[maxIdx1] -= abilities.get(agentIdx1).get(maxIdx1);
					tmpGalaxy[theOther[1]] += abilities.get(agentIdx1).get(theOther[1]);
					if (getSumOfGalaxy(tmpGalaxy) > getSumOfGalaxy(maxGalaxy) && isValidGalaxy(tmpGalaxy)) {
						maxGalaxy[0] = tmpGalaxy[0];
						maxGalaxy[1] = tmpGalaxy[1];
						maxGalaxy[2] = tmpGalaxy[2];
						break;
					}
				}
			} else {
				if(tmpGalaxy[maxIdx0] <= 0) {
					if(list2d.get(maxIdx0).isEmpty()) continue;
					int agentIdx2=list2d.get(maxIdx0).get(0);
					int maxIdx2 = abilities.get(agentIdx2).get(3);
					tmpGalaxy[maxIdx2] -= abilities.get(agentIdx2).get(maxIdx2);
					tmpGalaxy[maxIdx0] += abilities.get(agentIdx2).get(maxIdx0);
					if (getSumOfGalaxy(tmpGalaxy) > getSumOfGalaxy(maxGalaxy) && isValidGalaxy(tmpGalaxy)) {
						maxGalaxy[0] = tmpGalaxy[0];
						maxGalaxy[1] = tmpGalaxy[1];
						maxGalaxy[2] = tmpGalaxy[2];
						continue;
					}
				}
				else if (getSumOfGalaxy(tmpGalaxy) > getSumOfGalaxy(maxGalaxy) && isValidGalaxy(tmpGalaxy)) {
					maxGalaxy[0] = tmpGalaxy[0];
					maxGalaxy[1] = tmpGalaxy[1];
					maxGalaxy[2] = tmpGalaxy[2];
					break;
				}
			}
		}
		if(isValidGalaxy(maxGalaxy))
			return maxGalaxy;
		
		return null;
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int m = Integer.parseInt(br.readLine());
		for (int j = 1; j <= m; j++) {
			int n = Integer.parseInt(br.readLine());
			List<List<Integer>> abilities = initAbilities(n);
			List<List<Integer>> list2d = get2dList(n);
			for (int i = 0; i < n; i++) {
				String[] input = br.readLine().split(" ");
				abilities.get(i).add(Integer.parseInt(input[0]));
				abilities.get(i).add(Integer.parseInt(input[1]));
				abilities.get(i).add(Integer.parseInt(input[2]));
			}
			int sumOfAbilities = getSumOfAbilities(abilities);
			int[] galaxy = initailizeGalaxy(abilities, list2d);

			sortList2d(list2d, abilities);

			int[] rslt = modifyGalaxy(galaxy, abilities, list2d);
			if (rslt != null) {
				System.out.println("#"+j+" "+(sumOfAbilities - getSumOfGalaxy(rslt)));
			} else {
				System.out.println("#"+j+" -1");
			}

		}
	}
}

