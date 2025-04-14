package org.magister.matrix;

import org.magister.bubbleSort.KindOfBubbleSort;
import org.magister.helper.CalculationStatistic;
import org.magister.helper.Numberxx;
import org.magister.helper.NumberxxOperations;
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
    protected static final String INPUT_DIR = "test_dataSem2/input/matrix/";
    protected static final String OUTPUT_DIR = "test_dataSem2/output/matrix/";
    protected static final String CHARTS_DIR = "test_dataSem2/output/matrix/";
    protected static final int RUNS = 100;

  protected static final int[] DIMENSIONS = {2, 3 , 4 , 5  , 10 , 20 , 30    };

    // Lista zagregowanych wyników (dla każdego typu operacji i rozmiaru)
    protected final List<StatisticsResult> aggregatedResults = new ArrayList<>();

    public void performTests() {
        createDirectories();


        for (int dim : DIMENSIONS) {
            System.out.println("Testing matrix of dimension " + dim + "x" + dim);

            // Tworzymy macierze przy użyciu ustalonych ziaren:
            // seed 0 dla pierwszej macierzy oraz seed 1 dla drugiej
            Matrix<Numberxx> matrix1Generic = createRandomMatrix(dim, 0L);
            Matrix<Numberxx> matrix2Generic = createRandomMatrix(dim, 1L);
            Matrix1 matrix1Concrete = createRandomMatrix1(dim, 0L);
            Matrix1 matrix2Concrete = createRandomMatrix1(dim, 1L);
            saveMatrixToFile(matrix1Generic, INPUT_DIR + "matrix1_generic_" + dim + ".txt")  ;
            saveMatrixToFile(matrix2Generic, INPUT_DIR + "matrix2_generic_" + dim + ".txt");
            saveMatrix1ToFile(matrix1Concrete, INPUT_DIR + "matrix1_concrete_" + dim + ".txt");
            saveMatrix1ToFile(matrix2Concrete, INPUT_DIR + "matrix2_concrete_" + dim + ".txt");

        }

        // Zapisz zagregowane statystyki do pliku
        saveAggregatedStatisticsToFile();

        // Generuj wykresy na podstawie zagregowanych wyników przy użyciu Java2D
        generateCharts();
    }








    /**
     * Create a random matrix of specified dimension (generic implementation) using provided seed.
     */
    protected  Matrix<Numberxx> createRandomMatrix(int dimension, long seed) {
        Random randomLocal = new Random(seed);
        Numberxx[][] data = new Numberxx[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                data[i][j] = Numberxx.valueOf(Numberxx.nextInt(10));
            }
        }
        return new Matrix<>(data, new NumberxxOperations());
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
    <T extends Number> void saveMatrixToFile(Matrix matrix, String filename) {
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
            // Nagłówki
            writer.write(String.format("%-15s %-15s%n", "Direct Time (ns)", "Reflection Time (ns)"));
            writer.write(String.format("%-15s %-15s%n", "---------------", "-------------------"));

            // Dane
            for (int i = 0; i < genericTimes.size(); i++) {
                writer.write(String.format("%-15d %-15d%n", genericTimes.get(i), concreteTimes.get(i)));
            }
        } catch (IOException e) {
            System.err.println("Error saving results to file: " + e.getMessage());
        }
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
                        sr.operation, sr.dimension, sr.reflectMean, sr.reflectMedian,  sr.reflectStdDev,
                        sr.objectMean, sr.objectMedian,  sr.objectStdDev, sr.ratio
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

        // Marginesy wykresusa
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



}
