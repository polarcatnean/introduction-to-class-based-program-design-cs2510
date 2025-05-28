import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class Solution {
  public int[] twoSum(int[] nums, int target) {
    int[] results = new int[2];
    for (int i = 0; i < nums.length; i++) {
      for (int j = i + 1; j < nums.length; j++) {
        if (nums[i] + nums [j] == target) {
          return new int[]{i, j};
        }
      }
    }
    
    return results;
  }
  
  public int[] twoSumMap(int[] nums, int target) {
    int[] results = new int[2];
    Map<Integer, Integer> map = new HashMap<>();
    for (int i = 0; i < nums.length; i++) {
        int complement = target - nums[i];
        if (map.containsKey(complement)) {
            return new int[]{map.get(complement), i};
        }
        map.put(nums[i], i);
    }
    return results;
    
  }
}

class ExamplesCases {
  Solution s = new Solution();
  int[] test1 = {2,7,11,15};
  int[] test2 = {3,2,4};
  int[] test3 = {3,3};
  
  void testTwoSum() {
    System.out.println(Arrays.toString(s.twoSum(test1, 9))); // [0, 1]
    System.out.println(Arrays.toString(s.twoSum(test2, 6))); // [1, 2]
    System.out.println(Arrays.toString(s.twoSum(test3, 6))); // [0, 1]
  }
  
  public static void main(String[] args) {
    ExamplesCases tester = new ExamplesCases();
    tester.testTwoSum(); // Runs the test
  }
}