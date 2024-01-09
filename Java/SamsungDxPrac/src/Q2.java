import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Q2 {

	private static void addMaxAbility(int[] galaxy, int abilities[], int indexOfAgent, int[][] firstOfSecondAbility,
			int[][] secondOfSecondAbility) {
		int indexOfFirstAbility = -1;
		int[] otherIndex = new int[2];
		if (abilities[0] >= abilities[1] && abilities[0] >= abilities[2]) {
			indexOfFirstAbility = 0;
			otherIndex[0] = 1;
			otherIndex[1] = 2;
		} else if (abilities[1] >= abilities[0] && abilities[1] >= abilities[2]) {
			indexOfFirstAbility = 1;
			otherIndex[0] = 0;
			otherIndex[1] = 2;
		} else {
			indexOfFirstAbility = 2;
			otherIndex[0] = 0;
			otherIndex[1] = 1;
		}
		int[] abilityDiff = new int[3];
		abilityDiff[otherIndex[0]] = abilities[otherIndex[0]] - abilities[indexOfFirstAbility];
		abilityDiff[otherIndex[1]] = abilities[otherIndex[1]] - abilities[indexOfFirstAbility];

		galaxy[indexOfFirstAbility] += abilities[indexOfFirstAbility];
		for (int i = 0; i <= 1; i++) {
			if (abilities[otherIndex[i]] <= 0)
				continue;
			if (firstOfSecondAbility[otherIndex[i]][1] == 1
					|| firstOfSecondAbility[otherIndex[i]][1] <= abilityDiff[otherIndex[i]]) {
				secondOfSecondAbility[otherIndex[i]][0] = firstOfSecondAbility[otherIndex[i]][0];
				secondOfSecondAbility[otherIndex[i]][1] = firstOfSecondAbility[otherIndex[i]][1];
				firstOfSecondAbility[otherIndex[i]][0] = indexOfAgent;
				firstOfSecondAbility[otherIndex[i]][1] = abilityDiff[otherIndex[i]];
			} else if (secondOfSecondAbility[otherIndex[i]][1] == 1
					|| secondOfSecondAbility[otherIndex[i]][1] < abilityDiff[otherIndex[i]]) {
				secondOfSecondAbility[otherIndex[i]][0] = indexOfAgent;
				secondOfSecondAbility[otherIndex[i]][1] = abilityDiff[otherIndex[i]];
			}
		}
	}

	private static int modifyGalaxy(int[] galaxy, int[][] firstOfSecondAbility, int[][] secondOfSecondAbility) {
		int sumOfGalaxy = galaxy[0] + galaxy[1] + galaxy[2];
		int zeroIndex1 = -1, zeroIndex2 = -1;
		for (int i = 0; i < 3; i++) {
			if (galaxy[i] == 0) {
				if (zeroIndex1 == -1) {
					zeroIndex1 = i;
				} else
					zeroIndex2 = i;
			}
		}
		
		if (zeroIndex1 == -1 && zeroIndex2 == -1)
			;
		else if (zeroIndex2 == -1) {
			if (firstOfSecondAbility[zeroIndex1][1] == 1)
				return -1;
			sumOfGalaxy += firstOfSecondAbility[zeroIndex1][1];
		} else {
			if (firstOfSecondAbility[zeroIndex1][1] == 1 || firstOfSecondAbility[zeroIndex2][1] == 1)
				return -1;
			if (firstOfSecondAbility[zeroIndex1][0] != firstOfSecondAbility[zeroIndex2][0]) {
				sumOfGalaxy += firstOfSecondAbility[zeroIndex1][1] + firstOfSecondAbility[zeroIndex2][1];
			} else {
				if (secondOfSecondAbility[zeroIndex1][1] == 1 && secondOfSecondAbility[zeroIndex2][1] == 1) {
					return -1;
				} else if (secondOfSecondAbility[zeroIndex1][1] == 1) {
					sumOfGalaxy = firstOfSecondAbility[zeroIndex1][1] + secondOfSecondAbility[zeroIndex2][1];
				} else if (secondOfSecondAbility[zeroIndex2][1] == 1) {
					sumOfGalaxy = firstOfSecondAbility[zeroIndex2][1] + secondOfSecondAbility[zeroIndex1][1];
				} else {
					int maxAdd = Math.max(firstOfSecondAbility[zeroIndex1][1] + secondOfSecondAbility[zeroIndex2][1],
							firstOfSecondAbility[zeroIndex2][1] + secondOfSecondAbility[zeroIndex1][1]);
					sumOfGalaxy += maxAdd;
				}
			}
		}
		return sumOfGalaxy;
	}

	public static void main(String[] args) throws NumberFormatException, IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int m = Integer.parseInt(br.readLine());
		for (int j = 1; j <= m; j++) {
			int n = Integer.parseInt(br.readLine());
			int sumOfAllAbilities = 0;
			int[][] firstOfSecondAbility = new int[3][2]; // 각 능력별 (가장 높은 능력 - 2번째로 높은 능력)순의 1등을 유지 첫번째 열은 인덱스 두번째 열은
															// (가장 높은
															// 능력 - 2번째로 높은 능력).
			int[][] secondOfSecondAbility = new int[3][2]; // 각 능력별 (가장 높은 능력 - 2번째로 높은 능력)순의 2등을 유지.
			for (int i = 0; i < 3; i++) {
				firstOfSecondAbility[i][0] = -1;
				firstOfSecondAbility[i][1] = 1;
				secondOfSecondAbility[i][0] = -1;
				secondOfSecondAbility[i][1] = 1;
			}

			int[] galaxy = new int[3];
			int[][] abilities = new int[50][3];
			for (int i = 0; i < n; i++) {
				String[] input = br.readLine().split(" ");
				abilities[i][0] = Integer.parseInt(input[0]);
				abilities[i][1] = Integer.parseInt(input[1]);
				abilities[i][2] = Integer.parseInt(input[2]);
				sumOfAllAbilities += abilities[i][0] + abilities[i][1] + abilities[i][2];
				addMaxAbility(galaxy, abilities[i], i, firstOfSecondAbility, secondOfSecondAbility);
			}
//			System.out.println(Arrays.toString(galaxy));
//			for(int i = 0; i < 3; i++) {
//				System.out.println(Arrays.toString(firstOfSecondAbility[i]));
//				System.out.println(Arrays.toString(secondOfSecondAbility[i]));
//			}

			if (n < 3) {
				System.out.println("#" + (j) + " -1");
				continue;
			}

			int sumOfGalaxy = modifyGalaxy(galaxy, firstOfSecondAbility, secondOfSecondAbility);
			if (sumOfGalaxy == -1) {
				System.out.println("#" + (j) + " -1");
				continue;
			}
			System.out.println("#" + (j) + " " + (sumOfAllAbilities - sumOfGalaxy));

//			System.out.println("sumOfAllAbilities = "+sumOfAllAbilities);
//			System.out.println("galaxy = "+sumOfGalaxy);
		}
	}
}

//1,2등만 뽑아 두면 됨. 최악의 경우 2등을 최선의 경우 1등을 쓰기 때문.
//서로 다른 인덱스인 경우 1등만을,
//같은 인덱스인 경우 1,2 2,1 조합을 비교.
//각 어빌리티별