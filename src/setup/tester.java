package setup;

public class tester {
    public static void main(String[] args) {
        int[][] nums = range(3,6);
        for(int[] r : nums) {
            System.out.println(r[0] + " | " + r[1]);
        }
    }

    public static int[][] range(int total, int numofthreads) {
        int[][] ranges = new int[numofthreads][2];
        int plusones = total % numofthreads;
        int div = total / numofthreads;
        int start = 0;
        for(int i = 0; i < numofthreads; i++) {
            if(start > total-1) {
                ranges[i][0] = -1;
                continue;
            }
            ranges[i][0] = start;
            if(plusones > 0) {
                start += div + 1;
                plusones--;
            } else {
                start += div;
            }
            ranges[i][1] = start-1;
        }
        return ranges;
    }
}
