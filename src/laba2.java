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

public class laba2
{
    public JScrollPane sp1;
    public String[] columnsHeadertable;
    public String[][] datatable;
    public DefaultTableModel model;
    public JTable table;
    private final JTextField textField = new JTextField(20);

    private boolean is_t1 = true;

    JFrame a = new JFrame("FOOZBY и шоколадный сервис");
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
                default:
                    System.out.println("лошара, не та цифра");
                    break;
            }
            System.out.println("поточек завершился");
        }

    }

    public void rff() {
        try {
            System.out.println("засыпаю5...");
            Thread.sleep(5000);
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
        int rowCount = table.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            model.removeRow(0);
        }
    }

    public void mat() {

        //threads[1]
        textField.setBounds(50,500,200,30);


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
            is_t1 = true;
        });

        t2.addActionListener(e -> {
            columnsHeadertable = new String[]{"ID", "Дата", "IDр", "ФИОк", "Марка", "Год выпуска", "Пробег", "Неисправность"};
            datatable = new String[][]{};
            model = new DefaultTableModel(datatable, columnsHeadertable);
            table.setModel(model);
            sp1.setBounds(50, 120,1200,150);
            TableColumn column = null;
            column = table.getColumnModel().getColumn(0);
            column.setMaxWidth(30);
            is_t1 = false;
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

        a.add(sp1);
        a.add(t1);
        a.add(t2);
        a.add(save_to_file);
        a.add(read_from_file);
        a.add(save_to_xml);
        a.add(read_from_xml);
        a.add(textField);
        int width = 1400;
        int height = 900;
        a.setSize(width, height);
        a.setLayout(null);
        a.setVisible(true);
    }
}