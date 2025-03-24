package org.magister.helper;

import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.magister.matrix.KindOfMatrix;
import org.magister.vector.KindOfVector;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Vizualization {



    /**
     * BAR CHART WITH KIND
     */

    /**
     * @param results
     * @param kind
     * @throws IOException
     */







    public static void showOrSaveBarChartForRatioWithKind(List<StatisticsResult> results, KindOfMatrix kind , String CHARTS_DIR) throws IOException {
        // Tworzymy wykres z uwzględnieniem rodzaju macierzy w tytule
        CategoryChart chart = createBarChartForRatioWithKind(
                results,
                kind,
                "Porównanie Mean (Gen Ref vs. Gen Obj)",
                "Wymiar",
                "Czas [ns]"
        );
        // Zapisujemy do pliku z uwzględnieniem rodzaju macierzy w nazwie
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "RatioBarChart/" + "ratio_bar_chart_" + kind + ".png", BitmapEncoder.BitmapFormat.PNG);
    }

    /**
     *
     * @param results
     * @param kind
     * @param chartTitle
     * @param xAxisTitle
     * @param yAxisTitle
     * @return
     */


    public static CategoryChart createBarChartForRatioWithKind(
            List<StatisticsResult> results,
            KindOfMatrix kind,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle
    ) {
        // Filtruj wyniki tylko dla wskazanego rodzaju macierzy
        List<StatisticsResult> filteredResults = results.stream()
                .filter(r -> r.kindOfMatrix == kind)
                .collect(Collectors.toList());

        CategoryChart chart = new CategoryChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle + " - " + kind)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setXAxisLabelRotation(90);

        // Używaj przefiltrowanych wyników
        List<String> xLabels = filteredResults.stream()
                .map(r -> "(D=" + r.dimension + ")(Op=" + r.operation + ")")
                .toList();

        List<Double> ratioValues = filteredResults.stream()
                .map(r -> r.ratio)
                .toList();

        chart.addSeries("Gen Ref/Gen Obj Ratio - " + kind, xLabels, ratioValues);

        return chart;
    }











    /**
     *  Create Chart with Operation
     * @param results
     * @param operation
     * @throws IOException
     */






    private static XYChart createChartRatioVsDimWithOperation(List<StatisticsResult> results,
                                                              String chartTitle,
                                                              String xAxisTitle,
                                                              String yAxisTitle,
                                                              String operation) {
        // Tworzymy wykres XY
        XYChart chart = new XYChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Osobne serie dla każdego typu macierzy

               /*
    RANDOM,           // Losowa macierz
    RANDOM,           // Losowa macierz
    IDENTITY,         // Macierz jednostkowa (tożsamościowa)
    DIAGONAL,         // Macierz diagonalna
    SYMMETRIC,        // Macierz symetryczna
    ANTISYMMETRIC,    // Macierz antysymetryczna (skew-symetryczna)
    LOWER_TRIANGULAR, // Macierz trójkątna dolna
    UPPER_TRIANGULAR  // Macierz trójkątna górna
         */



        List<StatisticsResult> radomResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation.toString()))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.RANDOM)
                .toList();




        List<StatisticsResult> identityResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.IDENTITY)
                .toList();

        List<StatisticsResult> diagResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.DIAGONAL)
                .toList();

        List<StatisticsResult> symetricResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.SYMMETRIC)
                .toList();

        List<StatisticsResult> antiResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.ANTISYMMETRIC)
                .toList();


        List<StatisticsResult> lowTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.LOWER_TRIANGULAR)
                .toList();

        List<StatisticsResult> upperTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.UPPER_TRIANGULAR)
                .toList();






        // Dodaj serie osobno

        chart.addSeries("Random", radomResults.stream().mapToDouble(r -> r.dimension).toArray(),
                radomResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("IDENTITYC", identityResults.stream().mapToDouble(r -> r.dimension).toArray(),
                identityResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("DIAGONAL", diagResults.stream().mapToDouble(r -> r.dimension).toArray(),
                diagResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("ANTISYMMETRIC", antiResults.stream().mapToDouble(r -> r.dimension).toArray(),
                antiResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("SYMMETRIC", symetricResults.stream().mapToDouble(r -> r.dimension).toArray(),
                symetricResults.stream().mapToDouble(r -> r.ratio).toArray());


        chart.addSeries("LOWER_TRIANGULAR", lowTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                lowTriaResults.stream().mapToDouble(r -> r.ratio).toArray());

        chart.addSeries("UPPER_TRIANGULAR", upperTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                upperTriaResults.stream().mapToDouble(r -> r.ratio).toArray());

        return chart;

    }




    public static void showOrSaveChartRatioVsDimWithOperationStat(List<StatisticsResult> results,
                                                              String operation,
                                                              String CHARTS_DIR,
                                                              String whatkindofstatist) throws IOException {
        XYChart chart = createChartRatioVsDimWithOperation(
                results,
                "Wykres: " + whatkindofstatist + " (x) vs. Dim (y) dla operacji " + operation,
                "Wymiar (Dim)",
                whatkindofstatist,
                operation,
                whatkindofstatist
        );
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR  + whatkindofstatist + "_vs_dim_chartlinear" + operation + ".png", BitmapEncoder.BitmapFormat.PNG);

    }

    private static XYChart createChartRatioVsDimWithOperation(List<StatisticsResult> results,
                                                              String chartTitle,
                                                              String xAxisTitle,
                                                              String yAxisTitle,
                                                              String operation,
                                                              String whatkindofstatist) {
        // Tworzymy wykres XY
        XYChart chart = new XYChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Osobne serie dla każdego typu macierzy

        List<StatisticsResult> radomResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation.toString()))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.RANDOM)
                .toList();

        List<StatisticsResult> identityResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.IDENTITY)
                .toList();

        List<StatisticsResult> diagResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.DIAGONAL)
                .toList();

        List<StatisticsResult> symetricResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.SYMMETRIC)
                .toList();

        List<StatisticsResult> antiResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.ANTISYMMETRIC)
                .toList();

        List<StatisticsResult> lowTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.LOWER_TRIANGULAR)
                .toList();

        List<StatisticsResult> upperTriaResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfMatrix == KindOfMatrix.UPPER_TRIANGULAR)
                .toList();

        // Dynamicznie wybieramy pole na podstawie parametru whatkindofstatist
        // Dodaj serie osobno z wykorzystaniem refleksji lub switch

        chart.addSeries("Random", radomResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(radomResults, whatkindofstatist));

        chart.addSeries("IDENTITY", identityResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(identityResults, whatkindofstatist));

        chart.addSeries("DIAGONAL", diagResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(diagResults, whatkindofstatist));

        chart.addSeries("ANTISYMMETRIC", antiResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(antiResults, whatkindofstatist));

        chart.addSeries("SYMMETRIC", symetricResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(symetricResults, whatkindofstatist));

        chart.addSeries("LOWER_TRIANGULAR", lowTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(lowTriaResults, whatkindofstatist));

        chart.addSeries("UPPER_TRIANGULAR", upperTriaResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(upperTriaResults, whatkindofstatist));
        System.out.println( whatkindofstatist  );
        System.out.println("-------------------");
        return chart;
    }

    /**
     /**
     * Metoda pomocnicza do pobierania odpowiednich wartości statystyk na podstawie parametru
     */
    private static double[] getStatisticsValues(List<StatisticsResult> results, String statisticType) {
        return results.stream().mapToDouble(r -> {
            String type = statisticType.toLowerCase();

            if (type.equals("ratio")) {
                return r.ratio;
            } else if (type.equals("objectmean")) {
                return r.objectMean;
            } else if (type.equals("reflectmean")) {
                return r.reflectMean;
            } else if (type.equals("objectmedian")) {
                return r.objectMedian;
            } else if (type.equals("reflectmedian")) {
                return r.reflectMedian;
            } else if (type.equals("reflectstddev")) {
                return r.reflectStdDev;
            } else if (type.equals("objectstddev")) {
                return r.objectStdDev;
            } else if (type.equals("reflectq1")) {
                return r.reflectQ1;
            } else if (type.equals("reflectq3")) {
                return r.reflectQ3;
            } else if (type.equals("reflectiqr")) {
                return r.reflectIQR;
            } else if (type.equals("reflectcv")) {
                return r.reflectCV;
            } else if (type.equals("reflectskewness")) {
                return r.reflectSkewness;
            } else if (type.equals("reflectkurtosis")) {
                return r.reflectKurtosis;
            } else if (type.equals("objectq1")) {
                return r.objectQ1;
            } else if (type.equals("objectq3")) {
                return r.objectQ3;
            } else if (type.equals("objectiqr")) {
                return r.objectIQR;
            } else if (type.equals("objectcv")) {
                return r.objectCV;
            } else if (type.equals("objectskewness")) {
                return r.objectSkewness;
            } else if (type.equals("objectkurtosis")) {
                return r.objectKurtosis;
            }else {
                System.out.println("aaaaa");
                return Double.parseDouble(null);

            }
        }).toArray();
    }


    /**
     * Generuje i zapisuje wykres słupkowy dla podanej operacji.
     * Wykres zawiera serie odpowiadające poszczególnym rodzajom macierzy.
     *
     * @param results   lista wyników statystycznych
     * @param operation operacja, dla której ma być wykres (np. "add", "multiply", "subtract")
     * @param CHARTS_DIR katalog, do którego zostanie zapisany wykres
     * @throws IOException przy błędach zapisu pliku
     */
    public static void showOrSaveBarChartForOperation(List<StatisticsResult> results, String operation, String CHARTS_DIR, String statisticType) throws IOException {
        CategoryChart chart = createBarChartForOperation(
                results,
                operation,
                "Wykres słupkowy dla operacji " + operation,
                "Wymiar (Dim)",
                "Wartość (np. czas [ns] lub ratio)",
                statisticType // Przekazujemy nowy parametr
        );
        // Nazwa pliku zawiera nazwę operacji
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "bar_chartOO_" + operation + "_" + statisticType + ".png", BitmapEncoder.BitmapFormat.PNG);
    }
    /**
     * Tworzy wykres słupkowy, w którym dla podanej operacji są oddzielnie wyświetlone serie dla
     * każdego rodzaju macierzy (KindOfMatrix). Oś X zawiera wymiar macierzy, a oś Y – wartość statystyki.
     *
     * @param results    lista wyników statystycznych
     * @param operation  operacja (np. "add", "multiply", "subtract")
     * @param chartTitle tytuł wykresu
     * @param xAxisTitle tytuł osi X
     * @param yAxisTitle tytuł osi Y
     * @return skonfigurowany wykres słupkowy (CategoryChart)
     */
    public static CategoryChart createBarChartForOperation(
            List<StatisticsResult> results,
            String operation,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle,
            String statisticType // Nowy parametr
    ) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setXAxisLabelRotation(90);

        // Dla każdego rodzaju macierzy dodajemy osobną serię
        for (KindOfMatrix kind : KindOfMatrix.values()) {
            List<StatisticsResult> filteredResults = results.stream()
                    .filter(r -> r.operation.equalsIgnoreCase(operation))
                    .filter(r -> r.kindOfMatrix == kind)
                    .collect(Collectors.toList());

            if (filteredResults.isEmpty()) {
                continue; // pomijamy, gdy nie ma danych dla danego rodzaju
            }

            // Na osi X pojawią się etykiety z wymiarem macierzy
            List<String> xLabels = filteredResults.stream()
                    .map(r -> "D=" + r.dimension)
                    .collect(Collectors.toList());

            // Używamy metody getStatisticsValues do pobrania wartości
            double[] values = getStatisticsValues(filteredResults, statisticType);

            chart.addSeries(kind.toString(), xLabels, Arrays.stream(values).boxed().collect(Collectors.toList()));
        }

        return chart;
    }


    /**
     * Vector Bar Chart
     */

    public static void showOrSaveBarChartForRatioWithKindForVector(List<StatisticsResult> results, KindOfVector kind , String CHARTS_DIR ) throws IOException {
        // Tworzymy wykres z uwzględnieniem rodzaju macierzy w tytule
        CategoryChart chart = createBarChartForRatioWithKindForVector(
                results,
                kind,
                "Porównanie Mean (Gen Ref vs. Gen Obj)",
                "Wymiar",
                "Czas [ns]"
        );
        // Zapisujemy do pliku z uwzględnieniem rodzaju macierzy w nazwie
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "ratio_bar_chart_" + kind + ".png", BitmapEncoder.BitmapFormat.PNG);
    }

    /**
     *
     * @param results
     * @param kind
     * @param chartTitle
     * @param xAxisTitle
     * @param yAxisTitle
     * @return
     */


    public static CategoryChart createBarChartForRatioWithKindForVector(
            List<StatisticsResult> results,
            KindOfVector kind,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle
    ) {
        // Filtruj wyniki tylko dla wskazanego rodzaju macierzy
        List<StatisticsResult> filteredResults = results.stream()
                .filter(r -> r.kindOfVector == kind)
                .collect(Collectors.toList());

        CategoryChart chart = new CategoryChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle + " - " + kind)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setXAxisLabelRotation(90);

        // Używaj przefiltrowanych wyników
        List<String> xLabels = filteredResults.stream()
                .map(r -> "(D=" + r.dimension + ")(Op=" + r.operation + ")")
                .toList();

        List<Double> ratioValues = filteredResults.stream()
                .map(r -> r.ratio)
                .toList();

        chart.addSeries("Gen Ref/Gen Obj Ratio - " + kind, xLabels, ratioValues);

        return chart;
    }




    /**
     * V E C T O R
     */

    /**
     * Execute a function for  a Chart - Linear for  DiffrenStatistictVsDimWithOperationForVector
     * @param results
     * @param operation
     * @param CHARTS_DIR
     * @param whatkindofstatist
     * @throws IOException
     */


    public static void showOrSaveChartDiffrenStatistictVsDimWithOperationForVector(List<StatisticsResult> results,
                                                                                   String operation,
                                                                                   String CHARTS_DIR,
                                                                                   String whatkindofstatist) throws IOException {
        XYChart chart = createChartDiffrentStatisticsVsDimWithOperationForVector(
                results,
                "Wykres: " + whatkindofstatist + " (x) vs. Dim (y) dla operacji " + operation,
                "Wymiar (Dim)",
                whatkindofstatist,
                operation,
                whatkindofstatist
        );

        BitmapEncoder.saveBitmap(chart, CHARTS_DIR  + whatkindofstatist + "_vs_dim_chartlinear" + operation + ".png", BitmapEncoder.BitmapFormat.PNG);

    }

    /**
     *  Create a Chart - Linear for  DiffrenStatistictVsDimWithOperationForVector
     * @param results
     * @param chartTitle
     * @param xAxisTitle
     * @param yAxisTitle
     * @param operation
     * @param whatkindofstatist
     * @return
     */

    private static XYChart createChartDiffrentStatisticsVsDimWithOperationForVector(List<StatisticsResult> results,
                                                                                    String chartTitle,
                                                                                    String xAxisTitle,
                                                                                    String yAxisTitle,
                                                                                    String operation,
                                                                                    String whatkindofstatist) {
        // Tworzymy wykres XY
        XYChart chart = new XYChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        // Osobne serie dla każdego typu macierzy

        List<StatisticsResult> radomResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfVector == KindOfVector.RANDOM)
                .toList();

        List<StatisticsResult> zeroResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfVector == KindOfVector.ZERO)
                .toList();

        List<StatisticsResult> onesResults = results.stream()
                .filter(r -> r.operation.equalsIgnoreCase(operation))
                .filter(r -> r.kindOfVector == KindOfVector.ONES)
                .toList();



        // Dynamicznie wybieramy pole na podstawie parametru whatkindofstatist
        // Dodaj serie osobno z wykorzystaniem refleksji lub switch

        chart.addSeries("Random", radomResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(radomResults, whatkindofstatist));

        chart.addSeries("Zero", zeroResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(zeroResults, whatkindofstatist));

        chart.addSeries("Ones", onesResults.stream().mapToDouble(r -> r.dimension).toArray(),
                getStatisticsValues(onesResults, whatkindofstatist));
       // System.out.println( whatkindofstatist  );
       // System.out.println("-------------------");
        return chart;
    }


    /**
     *
     * @param results
     * @param operation
     * @param CHARTS_DIR
     * @param statisticType
     * @throws IOException
     */

    public static void showOrSaveBarChartForOperationVector(List<StatisticsResult> results, String operation, String CHARTS_DIR, String statisticType) throws IOException {
        CategoryChart chart = createBarChartForOperationVector(
                results,
                operation,
                "Wykres słupkowy dla operacji " + operation,
                "Wymiar (Dim)",
                "Wartość (np. czas [ns] lub ratio)",
                statisticType // Przekazujemy nowy parametr
        );
        // Nazwa pliku zawiera nazwę operacji
        BitmapEncoder.saveBitmap(chart, CHARTS_DIR + "bar_chartOO_" + operation + "_" + statisticType + ".png", BitmapEncoder.BitmapFormat.PNG);
    }

    /**
     * Tworzy wykres słupkowy, w którym dla podanej operacji są oddzielnie wyświetlone serie dla
     * każdego rodzaju wektora (KindOfVector). Oś X zawiera wymiar wektora, a oś Y – wartość statystyki.
     *
     * @param results    lista wyników statystycznych
     * @param operation  operacja (np. "add", "multiply", "subtract")
     * @param chartTitle tytuł wykresu
     * @param xAxisTitle tytuł osi X
     * @param yAxisTitle tytuł osi Y
     * @param statisticType typ statystyki do wyświetlenia
     * @return skonfigurowany wykres słupkowy (CategoryChart)
     */
    public static CategoryChart createBarChartForOperationVector(
            List<StatisticsResult> results,
            String operation,
            String chartTitle,
            String xAxisTitle,
            String yAxisTitle,
            String statisticType // Parametr określający typ statystyki
    ) {
        CategoryChart chart = new CategoryChartBuilder()
                .width(1800)
                .height(1000)
                .title(chartTitle)
                .xAxisTitle(xAxisTitle)
                .yAxisTitle(yAxisTitle)
                .build();

        chart.getStyler().setLegendPosition(Styler.LegendPosition.OutsideS);
        chart.getStyler().setXAxisLabelRotation(90);

        // Dla każdego rodzaju wektora dodajemy osobną serię
        for (KindOfVector kind : KindOfVector.values()) {
            List<StatisticsResult> filteredResults = results.stream()
                    .filter(r -> r.operation.equalsIgnoreCase(operation))
                    .filter(r -> r.kindOfVector == kind)
                    .collect(Collectors.toList());

            if (filteredResults.isEmpty()) {
                continue; // pomijamy, gdy nie ma danych dla danego rodzaju
            }

            // Na osi X pojawią się etykiety z wymiarem wektora
            List<String> xLabels = filteredResults.stream()
                    .map(r -> "D=" + r.dimension)
                    .collect(Collectors.toList());

            // Używamy metody getStatisticsValues do pobrania wartości
            double[] values = getStatisticsValues(filteredResults, statisticType);

            chart.addSeries(kind.toString(), xLabels, Arrays.stream(values).boxed().collect(Collectors.toList()));
        }

        return chart;
    }


}
