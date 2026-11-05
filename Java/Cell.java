public class Cell{
    private int value;
    private boolean is_flag;
    private boolean check;

    public Cell(){
        value=0;
        check=false;
        is_flag=false;
    }

    public void open(){
        check = true;
        is_flag = false;
    }

    public void flag(){
        is_flag = !(is_flag);
        System.out.printf("%b\n",is_flag);
    }

    public void bomb() {
        value = -1;
    }

    public void increase(){
        if(value > -1){
            value = value+1;
        }
    }

    public void clear_all() {
        value = 0;
        is_flag = false;
        check = false;
    }

    public int get_value(){
        return value;
    }

    public boolean get_is_flag(){
        return is_flag;
    }

    public boolean get_check(){
        return check;
    }

    public void drop_check(){
        check = false;
    }
}
