package org.magister.matrix;

import org.magister.helper.IntegerOperations;
import org.magister.helper.StatisticsResult;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public  class PerformanceTestMatrix {
    // Directory constants
    protected static final String INPUT_DIR = "test_dataGG/input/matrix/";
    protected static final String OUTPUT_DIR = "test_dataGG/output/matrix/";
    protected static final String CHARTS_DIR = "test_dataGG/output/matrix/wykres/";

    // Number of test runs for each case
    protected static final int RUNS = 100;

    // Matrix dimensions to test
   // protected static final int[] DIMENSIONS = {2, 3 , 4 , 5 , 6 , 10, 50, 100, 200 , 1000, 2000 };
   // protected static final int[] DIMENSIONS = {  100,50,10,6,5,4,3,2,1 };
    protected static final int[] DIMENSIONS = {2, 3 , 4 , 5 , 6 ,10 , 20 , 50 , 100  };

    // Lista zagregowanych wyników (dla każdego typu operacji i rozmiaru)
    protected final List<StatisticsResult> aggregatedResults = new ArrayList<>();

    /**
     * Main method to run performance tests.
     */
    public void performTests() {
        // Ensure directories exist
        createDirectories();

        // Test each matrix dimension
        for (int dim : DIMENSIONS) {
            System.out.println("Testing matrix of dimension " + dim + "x" + dim);

            // Tworzymy macierze przy użyciu ustalonych ziaren:
            // seed 0 dla pierwszej macierzy oraz seed 1 dla drugiej
            Matrix<Integer> matrix1Generic = createRandomMatrix(dim, 0L);
            Matrix<Integer> matrix2Generic = createRandomMatrix(dim, 1L);
            Matrix1 matrix1Concrete = createRandomMatrix1(dim, 0L);
            Matrix1 matrix2Concrete = createRandomMatrix1(dim, 1L);
            saveMatrixToFile(matrix1Generic, INPUT_DIR + "matrix1_generic_" + dim + ".txt")  ;
            saveMatrixToFile(matrix2Generic, INPUT_DIR + "matrix2_generic_" + dim + ".txt");
            saveMatrix1ToFile(matrix1Concrete, INPUT_DIR + "matrix1_concrete_" + dim + ".txt");
            saveMatrix1ToFile(matrix2Concrete, INPUT_DIR + "matrix2_concrete_" + dim + ".txt");

            // Zapisz porównanie macierzy (porównujemy te utworzone z seed 0)
            porownajIMZapiszMacierze(dim, matrix1Generic, matrix1Concrete);

            // Test operacji i agregacja statystyk
            aggregatedResults.add(testAdd(matrix1Generic, matrix2Generic, matrix1Concrete, matrix2Concrete, dim));

            // Zapisz macierze wejściowe do plików
            aggregatedResults.add(testSubtract(matrix1Generic, matrix2Generic, matrix1Concrete, matrix2Concrete, dim));
            aggregatedResults.add(testMultiply(matrix1Generic, matrix2Generic, matrix1Concrete, matrix2Concrete, dim));
            aggregatedResults.add(testAddGenericObjectVsReflect(matrix1Generic, matrix2Generic ,  matrix1Concrete , matrix1Concrete ,   dim));

        }

        // Wyświetl zagregowane statystyki w konsoli
        displayDetailedStatistics();

        // Zapisz zagregowane statystyki do pliku
        saveAggregatedStatisticsToFile();

        // Generuj wykresy na podstawie zagregowanych wyników przy użyciu Java2D
        generateCharts();
    }


    StatisticsResult testAddGenericObjectVsReflect(Matrix<Integer> matrix1,
                                                   Matrix<Integer> matrix2,
                                                   Matrix1 matrix1Concrete,
                                                   Matrix1 matrix2Concrete,
                                                   int dim) {
        List<Long> objectTester = new ArrayList<>();
        List<Long> reflectionTester = new ArrayList<>();

        // Warm-up runs
        for (int i = 0; i < 3; i++) {
            matrix1.add(matrix2);
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
        }

        // Main runs
        for (int i = 0; i < RUNS; i++) {
            long startReflectionOfGeneric = System.nanoTime();
            MatrixReflectionUtil.performOperation(matrix1, matrix2, "add");
            long endReflectionOfGeneric = System.nanoTime();
            objectTester.add(endReflectionOfGeneric - startReflectionOfGeneric);

            long startObjectOfGeneric = System.nanoTime();
            matrix1.add(matrix2);
            long endoObjectOfGeneric = System.nanoTime();
            reflectionTester.add(endoObjectOfGeneric - startObjectOfGeneric);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_add_of_reflection_generic" + dim + ".txt";
        saveResultsToFile(resultsFilename, objectTester, reflectionTester);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_add_of_reflection_generic" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(objectTester, reflectionTester, statsFilename, "Add", dim);
        System.out.println("Addition results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }


    StatisticsResult testAdd(Matrix<Integer> matrix1, Matrix<Integer> matrix2,
                             Matrix1 matrix1Concrete, Matrix1 matrix2Concrete, int dim) {
        List<Long> genericTimes = new ArrayList<>();
        List<Long> concreteTimes = new ArrayList<>();

        // Warm-up runs
        for (int i = 0; i < 3; i++) {
            matrix1.add(matrix2);
            matrix1Concrete.add(matrix2Concrete);
        }

        // Measured runs
        for (int i = 0; i < RUNS; i++) {
            long startGeneric = System.nanoTime();
            matrix1.add(matrix2);
            long endGeneric = System.nanoTime();
            genericTimes.add(endGeneric - startGeneric);

            long startConcrete = System.nanoTime();
            matrix1Concrete.add(matrix2Concrete);
            long endConcrete = System.nanoTime();
            concreteTimes.add(endConcrete - startConcrete);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_add_" + dim + ".txt";
        saveResultsToFile(resultsFilename, genericTimes, concreteTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_add_" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(genericTimes, concreteTimes, statsFilename, "Add", dim);
        System.out.println("Addition results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }

    StatisticsResult testSubtract(Matrix<Integer> matrix1, Matrix<Integer> matrix2,
                                  Matrix1 matrix1Concrete, Matrix1 matrix2Concrete, int dim) {
        List<Long> genericTimes = new ArrayList<>();
        List<Long> concreteTimes = new ArrayList<>();

        // Warm-up runs
        for (int i = 0; i < 3; i++) {
            matrix1.subtract(matrix2);
            matrix1Concrete.subtract(matrix2Concrete);
        }

        // Measured runs
        for (int i = 0; i < RUNS; i++) {
            long startGeneric = System.nanoTime();
            matrix1.subtract(matrix2);
            long endGeneric = System.nanoTime();
            genericTimes.add(endGeneric - startGeneric);

            long startConcrete = System.nanoTime();
            matrix1Concrete.subtract(matrix2Concrete);
            long endConcrete = System.nanoTime();
            concreteTimes.add(endConcrete - startConcrete);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_subtract_" + dim + ".txt";
        saveResultsToFile(resultsFilename, genericTimes, concreteTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_subtract_" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(genericTimes, concreteTimes, statsFilename, "Subtract", dim);
        System.out.println("Subtraction results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }

    StatisticsResult testMultiply(Matrix<Integer> matrix1, Matrix<Integer> matrix2,
                                  Matrix1 matrix1Concrete, Matrix1 matrix2Concrete, int dim) {
        List<Long> genericTimes = new ArrayList<>();
        List<Long> concreteTimes = new ArrayList<>();

        // Warm-up runs
        for (int i = 0; i < 3; i++) {
            matrix1.multiply(matrix2);
            matrix1Concrete.multiply(matrix2Concrete);
        }

        // Measured runs
        for (int i = 0; i < RUNS; i++) {
            long startGeneric = System.nanoTime();
            matrix1.multiply(matrix2);
            long endGeneric = System.nanoTime();
            genericTimes.add(endGeneric - startGeneric);

            long startConcrete = System.nanoTime();
            matrix1Concrete.multiply(matrix2Concrete);
            long endConcrete = System.nanoTime();
            concreteTimes.add(endConcrete - startConcrete);
        }

        String resultsFilename = OUTPUT_DIR + "matrix_performance_multiply_" + dim + ".txt";
        saveResultsToFile(resultsFilename, genericTimes, concreteTimes);
        String statsFilename = OUTPUT_DIR + "matrix_statistics_multiply_" + dim + ".txt";
        StatisticsResult stats = calculateAndSaveStatistics(genericTimes, concreteTimes, statsFilename, "Multiply", dim);
        System.out.println("Multiplication results saved to " + resultsFilename + " and " + statsFilename);
        return stats;
    }

    /**
     * Create a random matrix of specified dimension (generic implementation) using provided seed.
     */
    protected  Matrix<Integer> createRandomMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        Integer[][] data = new Integer[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = randomLocal.nextInt(10);
            }
        }
        return new Matrix<>(data, new IntegerOperations());
    }

    /**
     * Create a random matrix of specified dimension (concrete implementation) using provided seed.
     */
    protected Matrix1 createRandomMatrix1(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        int[][] data = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = randomLocal.nextInt(10);
            }
        }
        return new Matrix1(data);
    }

    /**
     * Save generic matrix to a text file.
     */
    <T extends Number> void saveMatrixToFile(Matrix<T> matrix, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            int rows = matrix.getRows();
            int cols = matrix.getCols();
            writer.write("Matrix Dimension: " + rows + "x" + cols + "\n\n");
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(matrix.getElement(i, j) + "\t");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving matrix to file: " + e.getMessage());
        }
    }

    /**
     * Save concrete matrix to a text file.
     */
    void saveMatrix1ToFile(Matrix1 matrix, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            int rows = matrix.getRows();
            int cols = matrix.getCols();
            writer.write("Matrix Dimension: " + rows + "x" + cols + "\n\n");
            int[][] data = matrix.getData();
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    writer.write(data[i][j] + "\t");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving matrix1 to file: " + e.getMessage());
        }
    }

    /**
     * Compare and save the matrices used in tests.
     * Porównanie odbywa się dla macierzy utworzonych z tym samym seed (0L).
     */
    public void porownajIMZapiszMacierze(int dimension, Matrix<Integer> matrixGeneric, Matrix1 matrixConcrete) {
        File dir = new File("wyniki");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String fileName = "wyniki/porownanie_macierzy_" + dimension + ".txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Porównanie macierzy dla wymiaru " + dimension + "x" + dimension + ":\n");
            writer.write("-------------------------------------------------\n");
            writer.write("Generic implementation (Matrix<Integer>):\n");
            writer.write(matrixGeneric.toString() + "\n\n");
            writer.write("Concrete implementation (Matrix1):\n");
            writer.write(matrixConcrete.toString() + "\n\n");
            writer.write("Użyty seed: 0L\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ensure necessary directories exist.
     */
    protected void createDirectories() {
        try {
            Files.createDirectories(Paths.get(INPUT_DIR));
            Files.createDirectories(Paths.get(OUTPUT_DIR));
            Files.createDirectories(Paths.get(CHARTS_DIR));
        } catch (IOException e) {
            System.err.println("Error creating directories: " + e.getMessage());
        }
    }

    /**
     * Save raw performance results to text file.
     */
    protected void saveResultsToFile(String filename, List<Long> genericTimes, List<Long> concreteTimes) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Generic Time\tConcrete Time\n");
            writer.write("-----------\t-------------\n");
            for (int i = 0; i < genericTimes.size(); i++) {
                writer.write(genericTimes.get(i) + "\t\t" + concreteTimes.get(i) + "\n");
            }
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
        }
    }

    /**
     * Oblicza statystyki (średnia, mediana, moda, odchylenie standardowe) i zapisuje wyniki do pliku.
     * Zwraca obiekt StatisticsResult z zagregowanymi danymi.
     */
    StatisticsResult calculateAndSaveStatistics(List<Long> genericTimes, List<Long> concreteTimes,
                                                String filename, String operation, int dimension) {
        DoubleSummaryStatistics genericStats = genericTimes.stream().mapToDouble(Long::doubleValue).summaryStatistics();
        DoubleSummaryStatistics concreteStats = concreteTimes.stream().mapToDouble(Long::doubleValue).summaryStatistics();

        double genericMean = genericStats.getAverage();
        double concreteMean = concreteStats.getAverage();
        double genericMedian = calculateMedian(genericTimes);
        double concreteMedian = calculateMedian(concreteTimes);
        long genericMode = calculateMode(genericTimes);
        long concreteMode = calculateMode(concreteTimes);
        double genericStd = calculateStandardDeviation(genericTimes);
        double concreteStd = calculateStandardDeviation(concreteTimes);
        double ratio = genericMean / concreteMean;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Statistics for generic implementation (Matrix<Integer>):\n");
            writer.write(String.format("Mean: %.2f ns\n", genericMean));
            writer.write(String.format("Median: %.2f ns\n", genericMedian));
            writer.write(String.format("Mode: %d ns\n", genericMode));
            writer.write(String.format("Min: %d ns\n", (long) genericStats.getMin()));
            writer.write(String.format("Max: %d ns\n", (long) genericStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", genericStd));
            writer.write("\n");
            writer.write("Statistics for concrete implementation (Matrix1):\n");
            writer.write(String.format("Mean: %.2f ns\n", concreteMean));
            writer.write(String.format("Median: %.2f ns\n", concreteMedian));
            writer.write(String.format("Mode: %d ns\n", concreteMode));
            writer.write(String.format("Min: %d ns\n", (long) concreteStats.getMin()));
            writer.write(String.format("Max: %d ns\n", (long) concreteStats.getMax()));
            writer.write(String.format("Standard Deviation: %.2f ns\n", concreteStd));
            writer.write("\n");
            writer.write(String.format("Ratio of mean times (generic/concrete): %.2f\n", ratio));
            writer.write(String.format("Performance difference: Concrete implementation is %.2f times faster than generic\n",
                    (ratio > 1) ? ratio : 1 / ratio));
        } catch (IOException e) {
            System.err.println("Error saving statistics to file: " + e.getMessage());
        }
        return new StatisticsResult(operation, dimension, genericMean, genericMedian, genericMode, genericStd,
                concreteMean, concreteMedian, concreteMode, concreteStd, ratio);
    }





    /**
     * Calculate median from a list of values.
     */
    double calculateMedian(List<Long> values) {
        List<Long> sorted = values.stream().sorted().collect(Collectors.toList());
        int size = sorted.size();
        if (size % 2 == 0) {
            return (sorted.get(size / 2 - 1) + sorted.get(size / 2)) / 2.0;
        } else {
            return sorted.get(size / 2);
        }
    }

    /**
     * Calculate mode (najczęściej występującą wartość) z listy wartości.
     */
    long calculateMode(List<Long> values) {
        Map<Long, Long> freq = values.stream()
                .collect(Collectors.groupingBy(v -> v, Collectors.counting()));
        return freq.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(0L);
    }

    /**
     * Calculate standard deviation from a list of values.
     */
    double calculateStandardDeviation(List<Long> values) {
        double mean = values.stream().mapToLong(Long::longValue).average().orElse(0.0);
        double variance = values.stream().mapToDouble(v -> Math.pow(v - mean, 2)).average().orElse(0.0);
        return Math.sqrt(variance);
    }

    /**
     * Wyświetl w konsoli zagregowane statystyki w sformatowanej tabeli.
     */
    void displayDetailedStatistics() {
        System.out.println("\n===== AGREGOWANE STATYSTYKI =====");
        System.out.printf("%-10s %-8s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-15s %-10s\n",
                "Operacja", "Dim", "Gen_Mean(ns)", "Gen_Median(ns)", "Gen_Mode(ns)", "Gen_StdDev(ns)",
                "Con_Mean(ns)", "Con_Median(ns)", "Con_Mode(ns)", "Con_StdDev(ns)", "Ratio");
        for (StatisticsResult sr : aggregatedResults) {
            System.out.printf("%-10s %-8d %-15.2f %-15.2f %-15d %-15.2f %-15.2f %-15.2f %-15d %-15.2f %-10.2f\n",
                    sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian, sr.reflectMode, sr.reflectStdDev,
                    sr.objectMean, sr.objectMedian, sr.objectMode, sr.objectStdDev, sr.ratio);
        }
        System.out.println("==================================\n");
    }

    StatisticsResult calculateAndSaveStatistics(List<Long> genericReflectTimes, List<Long> GenericObjectTimes,
                                                String filename, String operation, int dimension, KindOfMatrix kind) {
        return null;
    }

    /**
     * Zapisz zagregowane statystyki do pliku tekstowego.
     */
    void saveAggregatedStatisticsToFile() {
        String filename = CHARTS_DIR + "/aggregated_statistics.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("Operacja\tDim\tGen_Mean(ns)\tGen_Median(ns)\tGen_Mode(ns)\tGen_StdDev(ns)\t" +
                    "Con_Mean(ns)\tCon_Median(ns)\tCon_Mode(ns)\tCon_StdDev(ns)\tRatio\n");
            for (StatisticsResult sr : aggregatedResults) {
                writer.write(String.format(
                        "%s\t%d\t%.2f\t%.2f\t%d\t%.2f\t%.2f\t%.2f\t%d\t%.2f\t%.2f\n",
                        sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian, sr.reflectMode, sr.reflectStdDev,
                        sr.objectMean, sr.objectMedian, sr.objectMode, sr.objectStdDev, sr.ratio
                ));
            }
        } catch (IOException e) {
            System.err.println("Error saving aggregated statistics: " + e.getMessage());
        }
    }

    /**
     * Generuj trzy typy wykresów (słupkowy, liniowy, kołowy) przy użyciu wbudowanych klas Java2D.
     */
    void generateCharts() {
        generateBarChart();
        generateLineChart();
        generatePieChart();
    }

    /**
     * Generuje wykres słupkowy porównujący średnie czasy dla obu implementacji.
     * Zwiększamy dolny margines i obracamy etykiety o 45 stopni, aby były bardziej czytelne.
     */
    private void generateBarChart() {
        int width = 900, height = 700;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Zbierz kategorie oraz wartości
        List<String> categories = new ArrayList<>();
        List<Double> genericValues = new ArrayList<>();
        List<Double> concreteValues = new ArrayList<>();
        for (StatisticsResult sr : aggregatedResults) {
            String label = sr.operation + " (" + sr.dimension + "x" + sr.dimension + ")";
            categories.add(label);
            genericValues.add(sr.reflectMean);
            concreteValues.add(sr.objectMean);
        }

        // Znajdź maksymalną wartość do skalowania
        double maxValue = Math.max(
                genericValues.stream().mapToDouble(Double::doubleValue).max().orElse(1.0),
                concreteValues.stream().mapToDouble(Double::doubleValue).max().orElse(1.0)
        );

        // Marginesy wykresu (zwiększony bottomMargin dla czytelnych etykiet)
        int leftMargin = 80, rightMargin = 40, topMargin = 40, bottomMargin = 140;
        int chartWidth = width - leftMargin - rightMargin;
        int chartHeight = height - topMargin - bottomMargin;
        int n = categories.size();

        // Ustal szerokość słupka i odstępy
        // (musi starczyć miejsca na 2 słupki i odstęp pomiędzy nimi w każdej kategorii)
        int totalBarSpace = chartWidth / n;
        int barWidth = totalBarSpace / 3;
        int gap = barWidth; // odstęp między słupkami w tej samej kategorii

        // Rysuj słupki
        for (int i = 0; i < n; i++) {
            // Oblicz pozycję X dla danej kategorii
            int xCategory = leftMargin + i * totalBarSpace;

            // Wysokości słupków
            int genericBarHeight = (int) ((genericValues.get(i) / maxValue) * chartHeight);
            int concreteBarHeight = (int) ((concreteValues.get(i) / maxValue) * chartHeight);

            // Rysujemy słupek generic (niebieski)
            g.setColor(Color.BLUE);
            int xGeneric = xCategory;
            int yGeneric = topMargin + (chartHeight - genericBarHeight);
            g.fillRect(xGeneric, yGeneric, barWidth, genericBarHeight);

            // Rysujemy słupek concrete (czerwony)
            g.setColor(Color.RED);
            int xConcrete = xCategory + barWidth + gap;
            int yConcrete = topMargin + (chartHeight - concreteBarHeight);
            g.fillRect(xConcrete, yConcrete, barWidth, concreteBarHeight);

            // Rysujemy etykietę kategorii, obróconą o 45 stopni
            g.setColor(Color.BLACK);
            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            String catLabel = categories.get(i);

            // Środek kategorii (między słupkami)
            int centerX = xCategory + (barWidth + gap) / 2;

            // Punkt odniesienia do rysowania tekstu
            int labelX = centerX;
            int labelY = topMargin + chartHeight + 5;  // nieco ponad dolną krawędzią wykresu

            // Zachowaj oryginalną transformację
            var originalTransform = g.getTransform();

            // Przesuń układ współrzędnych na punkt (labelX, labelY)
            g.translate(labelX, labelY);

            // Obróć o -45 stopni
            g.rotate(-Math.PI / 4);

            // Rysuj tekst w (0,0)
            g.drawString(catLabel, 0, 0);

            // Przywróć transformację
            g.setTransform(originalTransform);
        }

        // Rysuj osie
        g.setColor(Color.BLACK);
        // Oś Y
        g.drawLine(leftMargin, topMargin, leftMargin, topMargin + chartHeight);
        // Oś X
        g.drawLine(leftMargin, topMargin + chartHeight, leftMargin + chartWidth, topMargin + chartHeight);

        // Zapis do pliku
        try {
            ImageIO.write(image, "png", new File(CHARTS_DIR + "/barChart.png"));
        } catch (IOException e) {
            System.err.println("Error saving bar chart: " + e.getMessage());
        }
        g.dispose();
    }

    /**
     * Generuje wykres liniowy pokazujący zależność wymiaru macierzy od stosunku średnich czasów.
     */
    private void generateLineChart() {
        int width = 800, height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // Marginesy wykresu
        int leftMargin = 80, rightMargin = 40, topMargin = 40, bottomMargin = 80;
        int chartWidth = width - leftMargin - rightMargin;
        int chartHeight = height - topMargin - bottomMargin;

        // Grupuj wyniki według operacji
        Map<String, List<StatisticsResult>> seriesMap = new HashMap<>();
        for (StatisticsResult sr : aggregatedResults) {
            seriesMap.computeIfAbsent(sr.operation, k -> new ArrayList<>()).add(sr);
        }

        // Określ zakres osi X na podstawie wymiarów
        int minDim = Arrays.stream(DIMENSIONS).min().orElse(10);
        int maxDim = Arrays.stream(DIMENSIONS).max().orElse(200);

        // Określ maksymalny stosunek
        double maxRatio = aggregatedResults.stream().mapToDouble(sr -> sr.ratio).max().orElse(1.0);

        // Rysuj osie
        g.setColor(Color.BLACK);
        g.drawLine(leftMargin, topMargin, leftMargin, topMargin + chartHeight);
        g.drawLine(leftMargin, topMargin + chartHeight, leftMargin + chartWidth, topMargin + chartHeight);

        // Kolory dla serii
        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN};
        int seriesIndex = 0;
        for (String op : seriesMap.keySet()) {
            List<StatisticsResult> list = seriesMap.get(op);
            list.sort(Comparator.comparingInt(sr -> sr.dimension));
            g.setColor(colors[seriesIndex % colors.length]);
            int prevX = -1, prevY = -1;
            for (StatisticsResult sr : list) {
                int x = leftMargin + (int) (((sr.dimension - minDim) / (double) (maxDim - minDim)) * chartWidth);
                int y = topMargin + chartHeight - (int) ((sr.ratio / maxRatio) * chartHeight);
                g.fillOval(x - 3, y - 3, 6, 6);
                if (prevX != -1) {
                    g.drawLine(prevX, prevY, x, y);
                }
                prevX = x;
                prevY = y;
            }
            seriesIndex++;
        }

        try {
            ImageIO.write(image, "png", new File(CHARTS_DIR + "/lineChart.png"));
        } catch (IOException e) {
            System.err.println("Error saving line chart: " + e.getMessage());
        }
        g.dispose();
    }

    /**
     * Generuje wykres kołowy pokazujący ogólny rozkład średnich czasów dla obu implementacji.
     */
    private void generatePieChart() {
        int width = 800, height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        double totalGeneric = aggregatedResults.stream().mapToDouble(sr -> sr.reflectMean).sum();
        double totalConcrete = aggregatedResults.stream().mapToDouble(sr -> sr.objectMean).sum();
        double total = totalGeneric + totalConcrete;
        int angleGeneric = (int) Math.round((totalGeneric / total) * 360);
        int angleConcrete = 360 - angleGeneric;

        int pieDiameter = 400;
        int pieX = (width - pieDiameter) / 2;
        int pieY = (height - pieDiameter) / 2;

        // Rysuj fragmenty wykresu kołowego
        g.setColor(Color.BLUE);
        g.fillArc(pieX, pieY, pieDiameter, pieDiameter, 0, angleGeneric);
        g.setColor(Color.RED);
        g.fillArc(pieX, pieY, pieDiameter, pieDiameter, angleGeneric, angleConcrete);
        g.setColor(Color.BLACK);
        g.drawOval(pieX, pieY, pieDiameter, pieDiameter);

        g.setFont(new Font("SansSerif", Font.PLAIN, 14));
        g.drawString("Generic: " + String.format("%.2f", totalGeneric), pieX, pieY + pieDiameter + 20);
        g.drawString("Concrete: " + String.format("%.2f", totalConcrete), pieX, pieY + pieDiameter + 40);

        try {
            ImageIO.write(image, "png", new File(CHARTS_DIR + "/pieChart.png"));
        } catch (IOException e) {
            System.err.println("Error saving pie chart: " + e.getMessage());
        }
        g.dispose();
    }

    StatisticsResult testAddGenericObjectVsReflect(Matrix<Integer> matrix1,
                                                   Matrix<Integer> matrix2,
                                                   int dim) {
        return null;
    }

    /**
     * Klasa przechowująca zagregowane statystyki dla danej operacji i wymiaru macierzy.
     */

}
