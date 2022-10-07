public class Item implements Comparable<Item>{
    // 表示该Item所属原片id的坐标
    private double x;
    private double y;

    private double width;
    private double length;
    private double area;

    // 表示产品id
    private int id;

    // 表示所属原片id
    private int plate_id;

    public Item(Item that)
    {
        this.id = that.id;
        this.length = that.length;
        this.width = that.width;
    }

    public Item(int id, double length, double width) {
        this.id = id;
        this.length = length;
        this.width = width;
        this.area = this.width * this.length;
    }

    @Override
    public String toString() {
        return plate_id + " " + id + " " + x + " " + y + " " + length + " " + width;
    }

    // 先按照长度排序, 从大到小排序，因为这些item使用边角料的概率不大，先凑好
    public int compareTo(Item that) {
        return Double.compare(that.area, this.area);
    }

    // 旋转的本质就是，交换length和width
    public void rotate() {
        double temp = this.length;
        this.length = this.width;
        this.width  = temp;
    }

    public double getLength() {
        return this.length;
    }

    public double getWidth() {
        return this.width;
    }


    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setPlate_id(int plate_id) {
        this.plate_id = plate_id;
    }
}
