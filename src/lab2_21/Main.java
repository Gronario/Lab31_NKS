package lab2_21;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.common.collect.Lists;
import org.paukov.combinatorics3.Generator;

public class Main {


    public static void main(String[] args) throws IOException {
        System.out.print("\nЛаб.робота2_Грона_Ю.О._ІО-83\n");
        List<List<Integer>> matrix = Matr_Input();
        List<Double> res = Inp_Get(matrix);
        final DFS Dfs_Path = new DFS(matrix.size());
        Dfs_Path.printResult(matrix);
        final List<List<Integer>> schemes = Table_of_probabilities(Dfs_Path.getPaths());
        final List<Double> probability_list = prob_calc(schemes, res);
        System.out.println("\nТаблиця станів системи:");
        for (int i = 0; i < schemes.size(); i++) {
            System.out.println(schemes.get(i) + " = " + probability_list.get(i));
        }
        System.out.printf("\nЙмовірність відмови P = %s\n", getSum(probability_list));
        System.out.printf("Інтенсивність відмов Lambda = %s\n", Lamda_calc(probability_list, 10));
        System.out.printf("Ймовірність відмови T = %s\n", 1 / Lamda_calc(probability_list, 10));
    }

    public static final String Probability = "C:\\Users\\yurag\\IdeaProjects\\Lab2_NKS\\src\\lab2_21\\probabilities.txt";
    public static final String Matrix = "C:\\Users\\yurag\\IdeaProjects\\Lab2_NKS\\src\\lab2_21\\matrix.txt";

    private static List<List<Integer>> Matr_Input() throws IOException {
        List<List<Integer>> Matr_Dynamic = Lists.newArrayList();
        List<String> input = Arrays.asList(
                new Scanner(
                        new File(new File(Main.Matrix).getPath()))
                        .useDelimiter("\\Z").next().split("\\r?\\n")
        );
        input
                .forEach(line -> Matr_Dynamic.add(
                        Arrays.stream(line.split(" "))
                                .mapToInt(Integer::parseInt)
                                .boxed()
                                .collect(Collectors.toList())
                ));

        return Matr_Dynamic;
    }
    private static List<Double> Inp_Get(final List<List<Integer>> matrix) throws FileNotFoundException {
        List<String> input = Arrays.asList(
                new Scanner(
                        new File(new File(Main.Probability).getPath()))
                        .useDelimiter("\\Z").next().split("\\r?\\n")
        );
        List<Double> res = new ArrayList<>();
        for (String value : input) {
            res.add(Double.parseDouble(value));
        }
        return res;
    }

    private static List<Double> prob_calc(final List<List<Integer>> schemas,
                                          final List<Double> inputPs
    ) {
        List<Integer> allNodes = new ArrayList<>(schemas.get(0));
        List<Double> res = new ArrayList<>();
        for (final List<Integer> schema : schemas) {
            double pVal = 1;
            List<Integer> tmp = new ArrayList<>(allNodes);
            for (final Integer node : schema) {
                pVal *= inputPs.get(node);
                tmp.remove(node);
            }
            for (final Integer integer : tmp) {
                pVal *= 1 - inputPs.get(integer);
            }
            res.add(pVal);
        }
        return res;
    }
    private static List<List<Integer>> Table_of_probabilities(final List<List<Integer>> paths) {
        List<List<Integer>> rows = new ArrayList<>();
        int maxNodes = 0;
        List<Integer> allNodes = new ArrayList<>();

        for (final List<Integer> path : new ArrayList<>(paths)) {
            List<Integer> tmp = new ArrayList<>(path);
            tmp.remove(0);
            tmp.remove(tmp.size() - 1);
            rows.add(tmp);
            if (max_calc(tmp) > maxNodes) {
                maxNodes = max_calc(tmp);
            }
        }
        IntStream.range(1, maxNodes + 1).forEach(allNodes::add);
        rows.add(allNodes);
        List<List<Integer>> test = new ArrayList<>(rows);
        for (final List<Integer> path : rows) {
            List<Integer> tmp = new ArrayList<>(allNodes);
            tmp.removeAll(path);
            for (int i = 1; i < tmp.size(); i++) {
                for (final List<Integer> comb : comb_get(tmp, i)) {
                    List<Integer> newTmp = new ArrayList<>(path);
                    newTmp.addAll(new ArrayList<>(comb));
                    newTmp.sort(Comparator.naturalOrder());
                    if (!rows.contains(newTmp)) {
                        test.add(new ArrayList<>(newTmp));
                    }
                }
            }
        }
        test.sort(Comparator.comparingInt(List::size));
        Collections.reverse(test);
        return test.stream().distinct().collect(Collectors.toList());
    }

    private static List<List<Integer>> comb_get(final List<Integer> tmp, final int times) {
        return Generator.combination(new HashSet<>(tmp))
                .simple(times)
                .stream()
                .collect(Collectors.toList());
    }

    private static Double Lamda_calc(final List<Double> probs, final int time) {
        return (-Math.log(getSum(probs)) / time);
    }

    private static double getSum(final List<Double> probs) {
        return probs.stream().mapToDouble(val -> val).sum();
    }

    private static Integer max_calc(final List<? extends Number> numbers) {
        return numbers.stream().mapToInt(Number::intValue).max().orElse(0);
    }
}
