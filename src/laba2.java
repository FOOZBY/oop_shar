import org.w3c.dom.*;

import javax.swing.*;
import javax.swing.table.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class laba2
{
    public JScrollPane sp1;
    public String[] columnsHeadertable;
    public String[][] datatable;
    public DefaultTableModel model;
    public JTable table;
    public JLabel label = new JLabel();

    private boolean is_t1 = true;

    JFrame a = new JFrame("Где-то стучит, посмотри, а?");
    int currentRow;

    public class TableMouseListener extends MouseAdapter {
        private final JTable table;

        public TableMouseListener(JTable table) {
            this.table = table;
        }
        public void mousePressed(MouseEvent event) {
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

    int choice=0;

    public class NewThread extends Thread {
        public void run()
        {
            System.out.println("поточек запустился");

            switch (choice) {
                case 0:
                    rff();
                    break;
                case 1:
                    stf();
                    break;
                case 2:
                    rfx();
                    break;
                case 3:
                    stx();
                    break;
                case 4:
                    try {
                        read_table_from_db();
                    } catch (ClassNotFoundException | SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 5:
                    try {
                        save_table_to_db();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case 6:
                    show_orders_per_month();
                    break;
                default:
                    System.out.println("не та цифра");
                    break;
            }
            System.out.println("поточек завершился");
        }

    }

    private void show_orders_per_month() {
        JFrame frame = new JFrame("заказы за месяц");

        JLabel label = new JLabel("Введите месяц и год в виде мм.гггг: ");
        label.setBounds(20, 20, 230, 20);

        JTextField textField = new JTextField();
        textField.setBounds(235,20,80,20);

        JButton submit =new JButton("Ввести");
        submit.setBounds(330,15,100,30);
        submit.addActionListener(e ->{
            String month="",year="",text;
            text = textField.getText();
            if (!text.isEmpty()) {
                month += text.charAt(0);
                month += text.charAt(1);
                year += text.charAt(3);
                year += text.charAt(4);
                year += text.charAt(5);
                year += text.charAt(6);

                JScrollPane sp1;
                String[] columnsHeadertable;
                String[][] datatable;
                DefaultTableModel model;
                JTable table;
                columnsHeadertable = new String[]{"ID", "Дата", "IDр", "ФИОк", "Марка", "Год выпуска", "Пробег", "Неисправность"};
                datatable = new String[][]{};
                model = new DefaultTableModel(datatable, columnsHeadertable);
                table = new JTable(model);
                sp1 = new JScrollPane(table);
                table.setModel(model);
                sp1.setBounds(50, 120, 1300, 150);
                TableColumn column = null;
                column = table.getColumnModel().getColumn(0);
                column.setMaxWidth(30);
                frame.add(sp1);

                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    throw new RuntimeException(ex);
                }
                ResultSet resultSet = null;
                try {
                    resultSet = statement.executeQuery("SELECT * FROM orders WHERE MONTH(date) = " + month + " and YEAR(date) = " + year);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                String[] row = new String[8];
                while (true) {
                    try {
                        if (!resultSet.next()) break;
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    for (int i = 0; i < 8; i++) {
                        try {
                            row[i] = resultSet.getString(i + 1);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    model.insertRow(model.getRowCount(), row);
                }
            }
        });

        frame.add(submit);
        frame.add(textField);
        frame.add(label);
        frame.setBounds(200, 250, 1400, 500);
        frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static final String USER_NAME = "root";
    public static final String PASSWORD = "root";
    public static final String URL = "jdbc:mysql://localhost:3306/mysql";

    public static Statement statement;
    public static Connection connection;

    static {
        try{
            connection = DriverManager.getConnection(URL,USER_NAME,PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    static {
        try{
            statement = connection.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void read_table_from_db() throws ClassNotFoundException, SQLException {
        menu_remove_all();
        String tableName;
        int col_amount;
        if (is_t1) {
            tableName = "Employees";
            col_amount = 3;
        } else {
            tableName = "Orders";
            col_amount = 8;
        }
        Class.forName("com.mysql.cj.jdbc.Driver");
        ResultSet resultSet = statement.executeQuery("SELECT * FROM " + tableName);
        String[] row = new String[col_amount];
        while (resultSet.next()) {
            for (int i = 0; i < col_amount; i++)
                row[i] = resultSet.getString(i+1);
            model.insertRow(model.getRowCount(), row);
        }
    }


    private void save_table_to_db() throws SQLException {
        String tableName;
        if (is_t1)
            tableName = "employees";
        else
            tableName = "orders";
        statement.executeUpdate("DROP TABLE "+tableName);
        System.out.println("table dropped");


        if (is_t1) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS " +tableName+ "(id int auto_increment primary key,name varchar(50) not null,spec varchar(50) not null)");
            for (int i = 0; i < model.getRowCount(); i++) {
                statement.executeUpdate(String.format("INSERT INTO %s VALUE(%s, '%s', '%s');",
                        tableName,
                        model.getValueAt(i, 0),
                        model.getValueAt(i, 1),
                        model.getValueAt(i, 2)));
            }
            System.out.println("table 1 saved");
        }else {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS "+tableName+" ("+
                    "id int auto_increment primary key,"+
                    "date DATE,"+
                    "id_empl int,"+
                    "name varchar(50) not null,"+
                    "car varchar(50) not null,"+
                    "car_year int,"+
                    "mileage int,"+
                    "issue varchar(50) not null)");
            for (int i = 0; i < model.getRowCount(); i++) {
                statement.executeUpdate(String.format("INSERT INTO %s VALUE(%s, '%s', '%s', '%s', '%s', '%s', '%s', '%s');",
                        tableName,
                        model.getValueAt(i, 0),
                        model.getValueAt(i, 1),
                        model.getValueAt(i, 2),
                        model.getValueAt(i, 3),
                        model.getValueAt(i, 4),
                        model.getValueAt(i, 5),
                        model.getValueAt(i, 6),
                        model.getValueAt(i, 7)));
            }
            System.out.println("table 2 saved");
        }
        System.out.println("saved!");
    }

    List<String> test_arr = new ArrayList<>();
    public void rff() {

        try {
            System.out.println("засыпаю5...");
            Thread.sleep(0);
            System.out.println("...просыпаюсь");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
            String fileName;
            int col_amount;
            if (is_t1) {
                fileName = "Employees.txt";
                col_amount = 3;
            } else {
                fileName = "Orders.txt";
                col_amount = 8;
            }
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                menu_remove_all();
                String line = reader.readLine();
                while (line != null) {
                    String[] row = new String[col_amount];
                    StringBuilder word = new StringBuilder();
                    test_arr.add(line);
                    int col = 0;
                    for (int j = 0; j < line.length(); j++) {
                        while (line.charAt(j) != '~') {
                            word.append(line.charAt(j));
                            j++;
                        }
                        row[col] = word.toString();
                        word = new StringBuilder();
                        col++;
                    }

                    //test_arr.add(row);
                    model.insertRow(model.getRowCount(), row);

                    // read next line
                    line = reader.readLine();
                }
            } catch (IOException exc) {
                System.out.println("неверно указано имя файла");
            }
    }

    public void stf () {
        try {
            System.out.println("засыпаю7...");
            Thread.sleep(7000);
            System.out.println("...просыпаюсь");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String fileName;
        if (is_t1)
            fileName = "Employees.txt";
        else
            fileName = "Orders.txt";

        try (FileWriter writer = new FileWriter(fileName)) {
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.write(String.valueOf(model.getValueAt(i, j)));
                    writer.write('~');
                }
                if (i != model.getRowCount() - 1)
                    writer.write('\n');
            }
            writer.flush();
        } catch (IOException exs) {
            JOptionPane.showMessageDialog(a, "Внимание", "Спасибо за внимание", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void stx () {
        try {
            System.out.println("засыпаю3...");
            Thread.sleep(3000);
            System.out.println("...просыпаюсь");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        String fileName;
        if (is_t1)
            fileName = "Employees.xml";
        else
            fileName = "Orders.xml";
        try {
            // create new `Document`
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document dom = builder.newDocument();
            // first create root element
            System.out.println();
            if (is_t1) {
                Element root = dom.createElement("Employees");
                dom.appendChild(root);
                for (int i = 0; i < model.getRowCount(); i++) {

                    Element row = dom.createElement("row");

                    Attr attr = dom.createAttribute("id");
                    attr.setValue((String) model.getValueAt(i, 0));
                    row.setAttributeNode(attr);

                    // now create child elements
                    Element name = dom.createElement("ФИО");
                    name.setTextContent((String) model.getValueAt(i, 1));
                    Element spec = dom.createElement("Специализация");
                    spec.setTextContent((String) model.getValueAt(i, 2));

                    // add child nodes to root node
                    root.appendChild(row);
                    row.appendChild(name);
                    row.appendChild(spec);
                }
            } else {
                Element root = dom.createElement("Orders");
                dom.appendChild(root);
                for (int i = 0; i < model.getRowCount(); i++) {

                    Element row = dom.createElement("row");

                    Attr attr = dom.createAttribute("id");
                    attr.setValue((String) model.getValueAt(i, 0));
                    row.setAttributeNode(attr);

                    // now create child elements
                    Element date = dom.createElement("Дата");
                    date.setTextContent((String) model.getValueAt(i, 1));
                    Element idw = dom.createElement("IDр");
                    idw.setTextContent((String) model.getValueAt(i, 2));
                    Element client_name = dom.createElement("ФИОк");
                    client_name.setTextContent((String) model.getValueAt(i, 3));
                    Element car_model = dom.createElement("Марка");
                    car_model.setTextContent((String) model.getValueAt(i, 4));
                    Element year_of_manufacture = dom.createElement("Год_выпуска");
                    year_of_manufacture.setTextContent((String) model.getValueAt(i, 5));
                    Element mileage = dom.createElement("Пробег");
                    mileage.setTextContent((String) model.getValueAt(i, 6));
                    Element issue = dom.createElement("Неисправность");
                    issue.setTextContent((String) model.getValueAt(i, 7));

                    // add child nodes to root node
                    root.appendChild(row);
                    row.appendChild(date);
                    row.appendChild(idw);
                    row.appendChild(client_name);
                    row.appendChild(car_model);
                    row.appendChild(year_of_manufacture);
                    row.appendChild(mileage);
                    row.appendChild(issue);
                }
            }
            // write DOM to XML file
            Transformer tr = TransformerFactory.newInstance().newTransformer();
            tr.setOutputProperty(OutputKeys.INDENT, "yes");
            tr.transform(new DOMSource(dom), new StreamResult(new File(fileName)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void rfx () {
        try {
            System.out.println("засыпаю10...");
            Thread.sleep(10000);
            System.out.println("...просыпаюсь");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        menu_remove_all();
        String fileName;
        int col_amount;
        if (is_t1) {
            fileName = "Employees.xml";
            col_amount = 3;
        } else {
            fileName = "Orders.xml";
            col_amount = 8;
        }
        if (is_t1) {
            try {
                // parse XML file to build DOM
                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                Document dom = builder.parse(new File(fileName));

                // normalize XML structure
                dom.normalizeDocument();

                // get root element
                Element root = dom.getDocumentElement();

                NodeList nodelist = dom.getElementsByTagName("row");

                for (int i = 0; i < nodelist.getLength(); i++) {

                    Node node = nodelist.item(i);
                    Element element = (Element) node;
                    // get staff's attribute
                    String[] row = new String[col_amount];
                    // get staff's attribute
                    row[0] = element.getAttribute("id");
                    // get text
                    row[1] = element.getElementsByTagName("ФИО").item(0).getTextContent();
                    row[2] = element.getElementsByTagName("Специализация").item(0).getTextContent();
                    model.insertRow(model.getRowCount(), row);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                // parse XML file to build DOM
                DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                        .newDocumentBuilder();
                Document dom = builder.parse(new File(fileName));

                // normalize XML structure
                dom.normalizeDocument();

                // get root element
                Element root = dom.getDocumentElement();
                NodeList nodelist = dom.getElementsByTagName("row");

                for (int i = 0; i < nodelist.getLength(); i++) {

                    Node node = nodelist.item(i);
                    Element element = (Element) node;
                    String[] row = new String[col_amount];
                    // get staff's attribute
                    row[0] = element.getAttribute("id");
                    // get text
                    row[1] = element.getElementsByTagName("Дата").item(0).getTextContent();
                    row[2] = element.getElementsByTagName("IDр").item(0).getTextContent();
                    row[3] = element.getElementsByTagName("ФИОк").item(0).getTextContent();
                    row[4] = element.getElementsByTagName("Марка").item(0).getTextContent();
                    row[5] = element.getElementsByTagName("Год_выпуска").item(0).getTextContent();
                    row[6] = element.getElementsByTagName("Пробег").item(0).getTextContent();
                    row[7] = element.getElementsByTagName("Неисправность").item(0).getTextContent();

                    model.insertRow(model.getRowCount(), row);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public void menu_add() {
        if (is_t1) {
            model.insertRow(table.getRowCount(), new Object[]{(table.getRowCount() + 1)});
        } else {
            model.insertRow(table.getRowCount(), new Object[]{(table.getRowCount() + 1)});
        }
    }

    public void menu_remove() {
        try {
            model.removeRow(currentRow);
        } catch (ArrayIndexOutOfBoundsException exc) {
            JOptionPane.showMessageDialog(a, "Не выбран элемент для удаления","Erorr",JOptionPane.WARNING_MESSAGE);
        }
    }

    public void menu_remove_all(){
        try {
            int rowCount = table.getRowCount();
            for (int i = 0; i < rowCount; i++) {
                model.removeRow(0);
            }
        }
        catch (NullPointerException exc)
        {
            return;
        }
    }

    public void mat() {
        a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //кнопки переключения таблиц
        JButton t1 = new JButton("Специалисты");
        t1.setBounds(50,50,200,40);
        JButton t2 = new JButton("Заказы");
        t2.setBounds(270,50,200,40);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItemAdd = new JMenuItem("Добавить строку");
        JMenuItem menuItemRemove = new JMenuItem("Удалить текущую строку");
        JMenuItem menuItemRemoveAll = new JMenuItem("Удалить все строки");
        popupMenu.add(menuItemAdd);
        popupMenu.add(menuItemRemove);
        popupMenu.add(menuItemRemoveAll);

        table = new JTable(model);
        sp1= new JScrollPane(table);
        table.setFillsViewportHeight(true);
        table.setComponentPopupMenu(popupMenu);

        table.addMouseListener(new TableMouseListener(table));
        menuItemAdd.addActionListener(e -> menu_add());
        menuItemRemove.addActionListener(e -> menu_remove());
        menuItemRemoveAll.addActionListener(e -> menu_remove_all());

        //кнопки переключения таблиц

        //переключение таблиц
        t1.addActionListener(e -> {
            columnsHeadertable = new String[]{"ID", "ФИО", "Специализация"};
            datatable = new String[][]{};
            model = new DefaultTableModel(datatable, columnsHeadertable);
            table.setModel(model);
            sp1.setBounds(50, 120,800,150);
            TableColumn column = null;
            column = table.getColumnModel().getColumn(0);
            column.setMaxWidth(30);
            ResultSet resultSet;
            try {
                resultSet = statement.executeQuery("SELECT COUNT(*) FROM employees");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String count;
            try {
                resultSet.next();
                count = resultSet.getString(1);
                System.out.println(count);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            label.setText("Количество записей в таблице: " + count);
            label.setBounds(1000, 300, 330, 20);
            is_t1 = true;

            try {
                read_table_from_db();
            } catch (ClassNotFoundException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        t2.addActionListener(e -> {
            columnsHeadertable = new String[]{"ID", "Дата", "IDр", "ФИОк", "Марка", "Год выпуска", "Пробег", "Неисправность"};
            datatable = new String[][]{};
            model = new DefaultTableModel(datatable, columnsHeadertable);
            table.setModel(model);
            sp1.setBounds(50, 120, 1300, 150);
            TableColumn column = null;
            column = table.getColumnModel().getColumn(0);
            column.setMaxWidth(30);

            ResultSet resultSet;
            try {
                resultSet = statement.executeQuery("SELECT COUNT(*) FROM orders");
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            String count;
            try {
                resultSet.next();
                count = resultSet.getString(1);
                System.out.println(count);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            label.setText("Количество записей в таблице: " + count);
            label.setBounds(1000, 300, 330, 20);

            is_t1 = false;

            try {
                read_table_from_db();
            } catch (ClassNotFoundException | SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        t1.doClick();

        JButton save_to_file = new JButton("Save to file");
        save_to_file.setBounds(490,50,200,40);
        save_to_file.addActionListener(e ->{
            choice = 1;
            NewThread thread = new NewThread();
            thread.start();
            JOptionPane.showMessageDialog(a, "Loading. . .", "Wait", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton read_from_file = new JButton("Read from file");
        read_from_file.setBounds(710,50,200,40);
        read_from_file.addActionListener(e -> {
            choice = 0;
            NewThread thread = new NewThread();
            System.out.println("иду в загрузку из файла");
            thread.start();
            JOptionPane.showMessageDialog(a, "Loading. . .", "Wait", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton save_to_xml = new JButton("Save to XML");
        save_to_xml.setBounds(930,50,200,40);
        save_to_xml.addActionListener(e -> {
            choice = 3;
            NewThread thread = new NewThread();
            thread.start();
            JOptionPane.showMessageDialog(a, "Loading. . .", "Wait", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton read_from_xml = new JButton("Read from XML");
        read_from_xml.setBounds(1150,50,200,40);
        read_from_xml.addActionListener(e -> {
            choice = 2;
            NewThread thread = new NewThread();
            thread.start();
            while (thread.isAlive())
                JOptionPane.showMessageDialog(a, "Loading. . .", "Wait", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton save_to_db = new JButton("Save to Database");
        save_to_db.setBounds(50,300,200,40);
        save_to_db.addActionListener(e -> {
            choice = 5;
            NewThread thread = new NewThread();
            thread.start();
            while (thread.isAlive())
                JOptionPane.showMessageDialog(a, "Loading. . .", "Wait", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton read_from_db = new JButton("Read from Database");
        read_from_db.setBounds(270,300,200,40);
        read_from_db.addActionListener(e -> {
            choice = 4;
            NewThread thread = new NewThread();
            thread.start();
            while (thread.isAlive())
                JOptionPane.showMessageDialog(a, "Loading. . .", "Wait", JOptionPane.INFORMATION_MESSAGE);
        });


        JButton show_orders_per_month = new JButton("Show orders per month");
        show_orders_per_month.setBounds(490,300,200,40);
        show_orders_per_month.addActionListener(e -> {
            choice = 6;
            NewThread thread = new NewThread();
            thread.start();
        });



        a.add(sp1);
        a.add(t1);
        a.add(t2);
        a.add(save_to_file);
        a.add(read_from_file);
        a.add(save_to_xml);
        a.add(read_from_xml);
        a.add(save_to_db);
        a.add(read_from_db);
        a.add(show_orders_per_month);
        a.add(label);
        int width = 1400;
        int height = 900;
        a.setBounds(100,100,width, height);
        a.setLayout(null);
        a.setVisible(true);
    }
}