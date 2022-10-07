public class Plate implements Comparable<Plate>{

    // 原片id
    private int id;

    // 原片所处的阶段
    private int stage;

    // x和y表示位置，当前部分在原始plate的起始点
    private double x;
    private double y;

    private double width;
    private double length;
    private double area;

    public Plate(int id, double length, double width, double x, double y, int stage) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.x = x;
        this.y = y;
        this.stage = stage;
        this.area = width * length;
    }

    public double diffWidth(Item item) {
        return this.width - item.getWidth();
    }

    // 原片按照面积从小到大排序
    public int compareTo(Plate that) {
        return Double.compare(that.area, this.area);
    }

    // debug: 可能double不一定等于0
    public boolean died() {
        return this.width == 0 || this.length == 0;
    }

    public String toString() {
        return "" + this.length + " " + this.width + " " + this.x + " " + this.y;
    }

    // 在确定选择
    public void handle_rotate(Item item) {
        // stage == 0, 则只考虑横切，且确保item.length > item.width
        if (stage == 0) {
            // 旋转后确保item横着放
            if (item.getLength() <= this.length && item.getWidth() <= this.width) {
                return;
            }
        }
        // stage == 1, 则只考虑竖切，则确保item.width > item.length
        else if (stage == 1) {
            // 旋转后确保item竖着放
            if (item.getLength() > item.getWidth()) {
                item.rotate();
            }
            if (item.getLength() <= this.length && item.getWidth() <= this.width) {
                return;
            }
            // 如果竖着放不行，则横着放
            if (item.getLength() < item.getWidth()) {
                item.rotate();
            }
            if (item.getLength() <= this.length && item.getWidth() <= this.width) {
                return;
            }
       }
        // stage == 2, 则只考虑横切，且确保this.length == item.length or item.width
        else {
            if (this.length == item.getLength() && item.getWidth() <= this.width) {
                return;
            }
            // 下面这种情况则需要优先旋转
            if (this.length == item.getWidth() && item.getLength() <= this.width) {
                item.rotate();
                return;
            }
        }
    }

    // 原片或者余料，是否能够放得下item, 该函数不可更改参数item
    public boolean fit(Item item) {
        Item item_cp = new Item(item);
        // stage == 0, 则只考虑横切，且确保item.length > item.width
        if (stage == 0) {
            // 旋转后确保item横着放
            if (item_cp.getLength() < item_cp.getWidth()) {
                item_cp.rotate();
            }
            if (item_cp.getLength() <= this.length && item_cp.getWidth() <= this.width) {
                return true;
            }
        }
        // stage == 1, 则只考虑竖切，则确保item.width > item.length
        else if (stage == 1) {
            // 旋转后确保item竖着放
            if (item_cp.getLength() > item_cp.getWidth()) {
                item_cp.rotate();
            }
            if (item_cp.getLength() <= this.length && item_cp.getWidth() <= this.width) {
                return true;
            }
            // 如果竖着放不行，则横着放
            if (item_cp.getLength() < item_cp.getWidth()) {
                item_cp.rotate();
            }
            if (item_cp.getLength() <= this.length && item_cp.getWidth() <= this.width) {
                return true;
            }
       }
        // stage == 2, 则只考虑横切，且确保this.length == item.length or item.width
        else {
            if (this.length == item_cp.getLength() && item_cp.getWidth() <= this.width) {
                return true;
            }
            // 下面这种情况则需要优先旋转
            if (this.length == item_cp.getWidth() && item_cp.getLength() <= this.width) {
                item_cp.rotate();
                return true;
            }
        }
        return false;
    }

    // 这里需要考虑到切割阶段数不超过3
    // 沿着长边切割
    public Plate[] cut(Item item) {
        item.setX(this.x);
        item.setY(this.y);
        item.setPlate_id(this.id);

        Plate[] plates = new Plate[2];

        // stage为0, 需要横切
        if (this.stage == 0) {
            plates[0] = new Plate(this.id, this.length, this.width - item.getWidth(),
                    this.x, this.y + item.getWidth(), 0);
            plates[1] = new Plate(this.id, this.length - item.getLength(), item.getWidth(),
                    this.x + item.getLength(), this.y, 1);
        }
        // stage为1, 需要竖切
        else if (this.stage == 1) {
            plates[0] = new Plate(this.id, this.length - item.getLength(), this.width,
                    this.x + item.getLength(), this.y, 1);
            plates[1] = new Plate(this.id, item.getLength(), this.width - item.getWidth(),
                    this.x, this.y + item.getWidth(), 2);
        }
        // stage为2, 需要横切，只剩一块
        else {
             plates[0] = new Plate(this.id, 0.0, 0.0,
                    this.x + item.getLength(), this.y, 1);
             plates[1] = new Plate(this.id, this.length, this.width - item.getWidth(),
                    this.x, this.y + item.getWidth(), 2);
        }

//        System.out.println(plates[0]);
//        System.out.println(plates[1]);
        return plates;
    }
}
