import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class laba2
{
    public JScrollPane sp1;
    public String[] columnsHeadertable;
    public String[][] datatable;
    public DefaultTableModel model;
    public JTable table;
    private final JTextField textField = new JTextField(20);

    private int t1_rows_count = 0;
    private int t2_rows_count = 0;

    private boolean is_t1 = true;

    JFrame a = new JFrame("FOOZBY и шоколадный сервис");
    int currentRow;

    public class TableMouseListener extends MouseAdapter
    {
        private final JTable table;

        public TableMouseListener(JTable table) {
            this.table = table;
        }
        public void mousePressed(MouseEvent event)
        {
            // selects the row at which point the mouse is clicked
            try {
            Point point = event.getPoint();
            currentRow = table.rowAtPoint(point);
            table.setRowSelectionInterval(currentRow, currentRow);
            }
            catch (IllegalArgumentException exc){
                return;
            }
        }
    }
    public void mat() {
        textField.setBounds(50,500,200,30);
        JButton t1 = new JButton("РАБоТЯГИ");
        t1.setBounds(50,50,200,40);
        JButton t2 = new JButton("Заказы");
        t2.setBounds(270,50,200,40);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemAdd = new JMenuItem("Add New Row");
        JMenuItem menuItemRemove = new JMenuItem("Remove Current Row");
        JMenuItem menuItemRemoveAll = new JMenuItem("Remove All Rows");
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemRemoveAll);

        table = new JTable(model);
        sp1= new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setComponentPopupMenu(popupMenu);

        table.addMouseListener(new TableMouseListener(table));
        menuItemAdd.addActionListener(e -> {
            if (is_t1)
            {
                model.insertRow(table.getRowCount(),new Object[]{(t1_rows_count + 1)});
                t1_rows_count++;
            }
            else
            {
                model.insertRow(table.getRowCount(),new Object[]{(t2_rows_count + 1)});
                t2_rows_count++;
            }
        });
        menuItemRemove.addActionListener(e -> {
            try {
                model.removeRow(currentRow);
            } catch (ArrayIndexOutOfBoundsException exc)
            {
                JOptionPane.showMessageDialog(a, "Не выбран элемент для удаления","Erorr",JOptionPane.WARNING_MESSAGE);
            }
        });


        menuItemRemoveAll.addActionListener(e -> {
            int rowCount = table.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                model.removeRow(0);
            }
        });

        //кнопки переключения таблиц

        //переключение таблиц
        t1.addActionListener(e -> {
            columnsHeadertable = new String[]{"ID", "ФИО", "Специализация"};
            datatable = new String[][]{};

            model = new DefaultTableModel(datatable, columnsHeadertable);
            table.setModel(model);
            sp1.setBounds(50, 120,800,150);
            is_t1 = true;
        });

        t2.addActionListener(e -> {
            columnsHeadertable = new String[]{"ID", "Дата", "IDр", "ФИОк", "Марка", "Год выпуска", "Пробег", "Неисправность"};
            datatable = new String[][]{};

            model = new DefaultTableModel(datatable, columnsHeadertable);
            table.setModel(model);
            sp1.setBounds(50, 120,1400,150);
            is_t1 = false;
        });
        t1.doClick();

        JButton save_to_file = new JButton("Save to file");
        save_to_file.setBounds(490,50,200,40);
        JButton read_from_file = new JButton("Read from file");
        read_from_file.setBounds(710,50,200,40);
        save_to_file.addActionListener(e -> {

            String fileName;
            if (is_t1) {
                fileName = "db1.txt";
            }
            else
                fileName = "db2.txt";

            try(FileWriter writer = new FileWriter(fileName))
            {
                for (int i = 0; i < model.getRowCount(); i++) {
                    for (int j = 0; j < model.getColumnCount(); j++) {
                        writer.write(String.valueOf(model.getValueAt(i,j)));
                        writer.write('~');
                    }
                    writer.write('\n');
                }
                writer.flush();
            } catch (IOException exs) {
                JOptionPane.showMessageDialog(a, "Внимание", "Спасибо за внимание", JOptionPane.INFORMATION_MESSAGE);
            }
        });





        a.add(sp1);
        a.add(t1);
        a.add(t2);
        a.add(save_to_file);
        a.add(read_from_file);
        a.add(textField);
        int width = 1600;
        int height = 900;
        a.setSize(width, height);
        a.setLayout(null);
        a.setVisible(true);
    }
}
