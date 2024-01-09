import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Q1 {
	
	private static int getLevel(int n) {
		for(int i = 0; i < 200; i++) {
			if(i*(i+1)/2 >= n) return i;
		}
		return -1;
	}
	private static int getDistanceFromStartInLevel(int n, int level) {
		return n - ((level-1)*level/2+1);
	}
	private static int getCost(int above, int bellow) {
		int aboveLevel = getLevel(above);
		int bellowLevel = getLevel(bellow);
		int levelDiff = bellowLevel - aboveLevel;
		
		int abovePositionInLevel = getDistanceFromStartInLevel(above, aboveLevel);
		int bellowPositionInLevel = getDistanceFromStartInLevel(bellow, bellowLevel);
		int positionDiff = bellowPositionInLevel-abovePositionInLevel;
		if(positionDiff >= 0 && positionDiff <= levelDiff) return levelDiff;
		if(positionDiff < 0) return levelDiff - positionDiff;
		return positionDiff;
	}
	
	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int n = Integer.parseInt(br.readLine());
		for(int i = 1; i <= n; i++) {
			String[] inputs = br.readLine().split(" ");
			int start = Integer.parseInt(inputs[0]);
			int end = Integer.parseInt(inputs[1]);
			int above = Math.min(start, end);
			int bellow = Math.max(start, end);
			System.out.println("#"+ i + " " + getCost(above, bellow));
		}
	}
}

//시점과 끝점 큰 쪽이 각각 시점에서의 거리가 0 에서 레벨 차이만큼은 레벨 개수안에 갈 수 있음.