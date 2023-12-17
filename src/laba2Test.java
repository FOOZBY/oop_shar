import org.junit.Assert;

import javax.swing.table.DefaultTableModel;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class laba2Test extends laba2{
    @org.junit.jupiter.api.Test
    public void rff1() {

        List<String> expected = new ArrayList<>();
        expected.add("1~Лосев Никита Русланович~механик~");
        expected.add("2~Денисова Аделина Георгиевна~маляр~");
        expected.add("3~Исаев Максим Артёмович~автоэлектрик~");
        expected.add("4~Воронов Андрей Олегович~моторист~");
        expected.add("5~Трошина Полина Эмильевна~приёмщик~");




        model = new DefaultTableModel(datatable, columnsHeadertable);
        rff();
        assertEquals( expected, test_arr);
    }
}