import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

public class SortingAlgorithm extends JPanel {
    private final int WIDTH = 800, HEIGHT = WIDTH * 9 / 16;
    public int SIZE = 150; // the number of sorting elements
    public float BAR_WIDTH = (float) WIDTH / SIZE; // bar width
    private float[] bar_height = new float[SIZE]; // height of bars
    private SwingWorker<Void, Void> shuffler, sorter;
    private int current_index, traversing_index; // needed for following coloring the items

    SortingAlgorithm() {
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initBarHeight(); // initialize the height of each bar
        // initShuffler(); // shuffle each bar
    }

    // setter for SIZE
    public void setSIZE(int SIZE) {
        this.SIZE = SIZE;
    }

    // getter for SIZE
    int getSIZE() {
        return SIZE;
    }

    // repaint() will automatically call this function
    // needed for coloring
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Create randomizer
        Random random = new Random();

        // Drawing the rectangles
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.WHITE);
        Rectangle2D.Float bar;

        for (int i = 0; i < getSIZE(); i++) {
            // random colors
            // final float hue = random.nextFloat();
            // final float saturation = 0.9f; //1.0 for brilliant, 0.0 for dull
            // final float luminance = 1.0f; //1.0 for brighter, 0.0 for black

            // g2d.setColor(Color.getHSBColor(hue, saturation, luminance));
            bar = new Rectangle2D.Float(i * BAR_WIDTH, 0, BAR_WIDTH - 1, bar_height[i]);
            g2d.fill(bar); // g2d.draw(bar);
        }

        // Color setter for the current object
        g2d.setColor(Color.RED);
        bar = new Rectangle2D.Float(current_index * BAR_WIDTH, 0, BAR_WIDTH, bar_height[current_index]);
        g2d.fill(bar);

        // Color setter for the traversing object
        g2d.setColor(Color.YELLOW);
        bar = new Rectangle2D.Float(traversing_index * BAR_WIDTH, 0, BAR_WIDTH, bar_height[traversing_index]);
        g2d.fill(bar);
    }

    public void insertionSort() {
        /*Insertion sort algorithm*/
        // Multithreading used for handling the sorting
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException { // function for calling multithreading
                // Insertion sort algorithm starts
                for (current_index = 1; current_index < getSIZE(); current_index++) {
                    traversing_index = current_index;
                    while (traversing_index > 0 && bar_height[traversing_index] < bar_height[traversing_index - 1]) {
                        swap(traversing_index, traversing_index - 1);
                        traversing_index--;

                        Thread.sleep(10); // controls the speed
                       repaint(); // we need it because we often replace the contents of a JPanel
                    }
                }
                current_index = 0;
                traversing_index = 0;

                return null;
            }
        };
    }

    public void bubbleSort() {
        /*Bubble sorting algorithm*/
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                for (current_index = 0; current_index < getSIZE(); current_index++) {
                    for (traversing_index = 1; traversing_index < (getSIZE() - current_index); traversing_index++) {
                        if (bar_height[traversing_index - 1] > bar_height[traversing_index]) {
                            swap(traversing_index, traversing_index - 1);
                            traversing_index--; // for animation

                            Thread.sleep(10); // controls the speed
                            repaint();
                        }
                    }
                }
                current_index = 0;
                traversing_index = 0;

                return null;
            }
        };
    }

    public void mergeSort() {
        /*Merge sorting algorithm*/
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                mergeSortHelper(0, getSIZE() - 1);

                current_index = 0;
                traversing_index = 0;

                return null;
            }
        };
    }

    private void mergeSortHelper(int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSortHelper(low, mid);
            mergeSortHelper(mid + 1, high);
            merge(low, mid, high);
        }
    }

    private void merge(int low, int mid, int high) {
        int leftSize = mid - low + 1;
        int rightSize = high - mid;

        float[] leftArray = new float[leftSize];
        float[] rightArray = new float[rightSize];

        // Copy elements into the left and right arrays
        for (int i = 0; i < leftSize; i++) {
            leftArray[i] = bar_height[low + i];
        }
        for (int j = 0; j < rightSize; j++) {
            rightArray[j] = bar_height[mid + 1 + j];
        }

        int i = 0, j = 0;
        int k = low;

        while (i < leftSize && j < rightSize) {
            if (leftArray[i] <= rightArray[j]) {
                bar_height[k] = leftArray[i];
                i++;
            } else {
                bar_height[k] = rightArray[j];
                j++;
            }
            k++;

            try {
                Thread.sleep(10); // controls the speed
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (i < leftSize) {
            bar_height[k] = leftArray[i];
            i++;
            k++;

            try {
                Thread.sleep(10); // controls the speed
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while (j < rightSize) {
            bar_height[k] = rightArray[j];
            j++;
            k++;

            try {
                Thread.sleep(10); // controls the speed
                repaint();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void selectionSort() {
        /*Selection sorting algorithm*/
        sorter = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                for (current_index = 0; current_index <getSIZE() - 1; current_index++) {
                    int min_index = current_index;
                    for (int traversing_index = current_index + 1; traversing_index < getSIZE(); traversing_index++) {
                        if (bar_height[traversing_index] < bar_height[min_index]) {
                            min_index = traversing_index;
                        }
                    }
                    swap(current_index, min_index);
                    Thread.sleep(10); // controls the speed
                    repaint(); // we need it because we often replace the contents of a JPanel
                }

                current_index = 0;
                traversing_index = 0;

                return null;
            }
        };
    }

    public void initShuffler() {
        /*Shuffles each bar*/
        shuffler = new SwingWorker<>() {
            @Override
            public Void doInBackground() throws InterruptedException {
                int middle = getSIZE() / 2;
                for (int i = 0, j = middle; i < middle; i++, j++) {
                    int random_index = new Random().nextInt(getSIZE());
                    swap(i, random_index);

                    random_index = new Random().nextInt(getSIZE());
                    swap(j, random_index);

                    Thread.sleep(10); // controls the speed
                    repaint(); // we need it because we often replace the contents of a JPanel
                }
                return null;
            }

            // after finishing the process
            @Override
            public void done() {
                super.done();
                sorter.execute();
            }
        };
        shuffler.execute();
    }

    public void initBarHeight() {
        /*Initialize the height of each bar*/
        float interval = (float) HEIGHT / getSIZE();
        for (int i = 0; i < getSIZE(); i++) {
            bar_height[i] = i * interval;
        }
    }

    public void swap(int indexA, int indexB) {
        /*Swaps the elements*/
        float temp = bar_height[indexA];
        bar_height[indexA] = bar_height[indexB];
        bar_height[indexB] = temp;
    }
}
