import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static final double PLATE_LENGTH = 2440;
    static final double PLATE_WIDTH  = 1220;

    public static Plate find_plate(Set<Plate> set_0, Set<Plate> set_1, Set<Plate> set_2, Item item) {
        // best_plate == null表示原片余料不满足
        // 其实可以用优先队列优化一下，面积从大到小排序，后面不合适就不遍历了
        Plate best_plate = null;
        for (Plate plate : set_2) {
            // 在放得下的plate中找出width最接近的
            if (plate.fit(item)) {
                if (best_plate == null) {
                    best_plate = plate;
                }
                else if (best_plate.diffWidth(item) > plate.diffWidth(item)) {
                    best_plate = plate;
                }
            }
        }
        if (best_plate != null) {
            return best_plate;
        }
        for (Plate plate : set_1) {
            // 在放得下的plate中找出width最接近的
            if (plate.fit(item)) {
                if (best_plate == null) {
                    best_plate = plate;
                }
                else if (best_plate.diffWidth(item) > plate.diffWidth(item)) {
                    best_plate = plate;
                }
            }
        }
        if (best_plate != null) {
            return best_plate;
        }
        for (Plate plate : set_0) {
            // 在放得下的plate中找出width最接近的
            if (plate.fit(item)) {
                if (best_plate == null) {
                    best_plate = plate;
                }
                else if (best_plate.diffWidth(item) > plate.diffWidth(item)) {
                    best_plate = plate;
                }
            }
        }
        return best_plate;
    }

    public static void main(String[] args) throws IOException {
        // System.out.println("hello");
        int sz_item = 0; // 表示有多少Item
        int sz_plate = 0;

        // 用于将所有的item保存起来
        List<Item> ans = new LinkedList<>();

        PriorityQueue<Item> pq_item = new PriorityQueue<>();

        String filename = args[0];
        // 处理输入，放入优先队列中，得到了长度按从大到小排序的Item
        String filePath = filename + ".csv";
        try (FileReader reader = new FileReader(filePath)) {
            BufferedReader br = new BufferedReader(reader);
            String line;
            // 去掉表头
            line = br.readLine();
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                String[] temp1 = line.split(","); // 分割字符串
                int item_id = Integer.parseInt(temp1[0]);
                double item_length = Double.parseDouble(temp1[3]);
                double item_width  = Double.parseDouble(temp1[4]);

                Item item = new Item(item_id, item_length, item_width);
                pq_item.add(item);
                ans.add(item);
                sz_item++;
            }
        } catch (NumberFormatException | IOException e1) {
            e1.printStackTrace();
        }

        // set_0用来保存所有plate(stage = 0)
        // set_1用来保存所有plate(stage = 1)
        // set_2用来保存所有plate(stage = 2)
        Set<Plate> set_0 = new HashSet<>();
        Set<Plate> set_1 = new HashSet<>();
        Set<Plate> set_2 = new HashSet<>();

        // 当item优先队列未满时，需要继续裁剪
        while (!pq_item.isEmpty()) {
            Item item = pq_item.poll();

            Plate best_plate = find_plate(set_0, set_1, set_2, item);

            // 需要拿一个新的原片
            if (best_plate == null) {
                Plate new_plate = new Plate(++sz_plate, PLATE_LENGTH, PLATE_WIDTH, 0, 0, 0);
                for (Plate p : new_plate.cut(item)) {
                    if (!p.died()) {
                        if (p.getStage() == 2)
                            set_2.add(p);
                        else if (p.getStage() == 1)
                            set_1.add(p);
                        else
                            set_0.add(p);
                    }
                }
            }
            else {
                // 切割之前进行旋转处理
                best_plate.handle_rotate(item);
                for (Plate p : best_plate.cut(item)) {
                    if (!p.died()) {
                        if (p.getStage() == 2)
                            set_2.add(p);
                        else if (p.getStage() == 1)
                            set_1.add(p);
                        else
                            set_0.add(p);
                    }
                }
                if (set_0.contains(best_plate))
                    set_0.remove(best_plate);
                else if (set_1.contains(best_plate))
                    set_1.remove(best_plate);
                else
                    set_2.remove(best_plate);
            }
        }

        String resFile = filename + "result.csv";
        File file = new File(resFile);
        BufferedWriter writeText = null;
        try {
            //通过BufferedReader类创建一个使用默认大小输出缓冲区的缓冲字符输出流
            writeText = new BufferedWriter(new FileWriter(resFile, false));
            //调用write的方法将字符串写到流中
            for (Item item : ans) {
                // System.out.println(item);
                if (!file.exists()) {
                    file.createNewFile();
                }
                writeText.append(item.toString().replace(' ',','));
                writeText.newLine();    //换行
                writeText.flush();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }finally {
            writeText.close();
        }
        System.out.println(filename + " " + sz_plate);
        /*
        for (Item item : ans) {
            System.out.println(item);
        }
        */
        /*
        while (!pq_item.isEmpty()) {
            Item item = pq_item.poll();
            System.out.println(item);
        }
        */
    }

}
