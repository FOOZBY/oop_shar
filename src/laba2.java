import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JFrame;

public class laba2
{

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
            Point point = event.getPoint();
             currentRow = table.rowAtPoint(point);
            table.setRowSelectionInterval(currentRow, currentRow);
        }
    }
    public void meat()
    {

        JButton t1 = new JButton("РАБоТЯГИ");
        JButton t2 = new JButton("Выполненные работы");
        t1.setBounds(50,50,200,40);
        t2.setBounds(270,50,200,40);



        String[] columnsHeadertable1 = {"ID", "ФИО", "Специальность"};
        String[][] datatable1 ={};
        DefaultTableModel model1 = new DefaultTableModel(datatable1,columnsHeadertable1);
        JTable table1 = new JTable(model1);
        JScrollPane sp1 = new JScrollPane(table1);
        sp1.setBounds(50, 120,800,150);

        String[] columnsHeadertable2 = {"ID", "Дата", "IDр", "ФИОк", "Марка", "Год выпуска", "Пробег", "Неисправность"};
        String[][] datatable2 ={};
        DefaultTableModel model2 = new DefaultTableModel(datatable2,columnsHeadertable2);
        JTable table2 = new JTable(model2);
        JScrollPane sp2 = new JScrollPane(table2);
        sp2.setBounds(50, 120,1400,150);
        sp2.setVisible(false);
        final boolean[] table1_vis = {true};

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemAdd = new JMenuItem("Add New Row");
        JMenuItem menuItemRemove = new JMenuItem("Remove Current Row");
        JMenuItem menuItemRemoveAll = new JMenuItem("Remove All Rows");
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemRemoveAll);


        table1.setComponentPopupMenu(popupMenu);
        table2.setComponentPopupMenu(popupMenu);



        table1.addMouseListener(new TableMouseListener(table1));
        table2.addMouseListener(new TableMouseListener(table2));
        menuItemAdd.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if (table1_vis[0])
                    model1.insertRow(table1.getRowCount(),new Object[]{table1.getRowCount()+1});
                else
                    model2.insertRow(table2.getRowCount(),new Object[]{table2.getRowCount()+1});
            }
        });
        menuItemRemove.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(table1_vis[0])
                    model1.removeRow(currentRow);
                else
                    model2.removeRow(currentRow);
            }
        });
        menuItemRemoveAll.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                if(table1_vis[0]) {
                    int rowCount = table1.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        model1.removeRow(0);
                    }
                }
                else
                {
                    int rowCount = table2.getRowCount();
                    for (int i = 0; i < rowCount; i++) {
                        model2.removeRow(0);
                    }
                }
            }
        });



        TableColumn column;
        column = table1.getColumnModel().getColumn(0);
        column.setMaxWidth(30);
        column = table2.getColumnModel().getColumn(0);
        column.setMaxWidth(30);
        column = table2.getColumnModel().getColumn(2);
        column.setMaxWidth(30);


        JButton addtot1 = new JButton("Добавить РАБоТЯГУ");
        addtot1.setBounds(1300,800,200,40);
        addtot1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model1.insertRow(table1.getRowCount(),new Object[]{table1.getRowCount()+1});
                table1_vis[0] = true;
            }
        });

        JButton addtot2 = new JButton("Добавить заказ");
        addtot2.setBounds(1300,800,200,40);
        addtot2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model2.insertRow(table2.getRowCount(),new Object[]{table2.getRowCount()+1});
                table1_vis[0] = false;
            }
        });



        //переключение таблиц
        t1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sp1.setVisible(true);
                sp2.setVisible(false);
                addtot1.setVisible(true);
                addtot2.setVisible(false);
            }
        });

        t2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sp1.setVisible(false);
                sp2.setVisible(true);
                addtot2.setVisible(true);
                addtot1.setVisible(false);
            }
        });


        
        a.add(addtot1);
        a.add(addtot2);
        a.add(sp1);
        a.add(sp2);
        a.add(t1);
        a.add(t2);
        int width = 1600;
        int height = 900;
        a.setSize(width, height);
        a.setLayout(null);
        a.setVisible(true);
    }
}
