import java.util.Arrays;
import java.util.Random;

public class laba1
{
    private int N = 20;
    private Integer[] arr;
    laba1(Integer[] args)
    {
        this.arr = args;
    }
    public void sorting_arr()
    {
        for (int i = 0; i < N; i++) {
            Random rnd = new Random();
            arr[i] = rnd.nextInt(100);
        }
        for (int i = 0; i < N; i++) {
            System.out.println(arr[i]);
        }
        Arrays.sort(arr);
        System.out.println("Sorted: ");
        for (int i = 0; i < N; i++) {
            System.out.println(arr[i]);
        }
    }
}
