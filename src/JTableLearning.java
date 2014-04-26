import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

public class JTableLearning extends JFrame {
    private JTable table;
    private JTable jg_table;
    private JScrollPane jsp;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    JTableLearning frame = new JTableLearning();

                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public JTableLearning() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setBounds(100, 100, 100,100);
        getContentPane().setLayout(new BorderLayout(0, 0));

        JScrollPane scrollPane = new JScrollPane();

        TableModel dataModel = new AbstractTableModel() {
            public int getColumnCount() {
                return 5;
            }

            public int getRowCount() {
                return 5;
            }

            public Object getValueAt(int row, int col) {
                return new Integer(row * col);
            }
        };

        AbstractTableModel tm;
        final Vector<List<String>> vect;// ����һ����������
        //    JScrollPane jsp;// ����һ�������ܶ���
        final String title[] = { "ְ����", "ְ����", "�Ա�", "��������", "����" }; // ��ά������
        vect = new Vector();// ʵ��������
        tm = new AbstractTableModel() {
            public int getColumnCount() {
                return title.length;
            }// ȡ�ñ������

            public int getRowCount() {
                return vect.size();
            }// ȡ�ñ������

            public Object getValueAt(int row, int column) {
                if (!vect.isEmpty())
                    return ((Vector) vect.elementAt(row)).elementAt(column);
                else
                    return null;
            }// ȡ�õ�Ԫ���е�����ֵ

            public String getColumnName(int column) {
                return title[column];
            }// ���ñ������

            public void setValueAt(Object value, int row, int column) {
            } // ����ģ�Ͳ��ɱ༭���÷�������Ϊ��

            public Class getColumnClass(int c) {
                return getValueAt(0, c).getClass();
            }// ȡ��������������

            public boolean isCellEditable(int row, int column) {
                return false;
            }// ���õ�Ԫ�񲻿ɱ༭��Ϊȱʡʵ��
        };


        vect.removeAllElements();// ��ʼ����������
        tm.fireTableStructureChanged();// ���±������

        Vector<String> list = new Vector();

        list.add("fda");
        list.add("aaa");
        list.add("ssa");
        list.add("dda");
        list.add("mma");

        vect.addElement(list);
        vect.addElement(list);vect.addElement(list);vect.addElement(list);
        vect.addElement(list);vect.addElement(list);vect.addElement(list);
        tm.fireTableStructureChanged();
        // };
        // ���Ʊ��
        jg_table = new JTable(tm);// �����Լ�������ģ��
        jg_table.setToolTipText("��ʾȫ����ѯ���");
        // ���ð�����ʾ
        jg_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // ���ñ������ߴ�ģʽ
        jg_table.setCellSelectionEnabled(false);
        // ���õ�Ԫ��ѡ��ʽ
        jg_table.setShowVerticalLines(true);//
        // �����Ƿ���ʾ��Ԫ���ķָ���
        jg_table.setShowHorizontalLines(true);

        jsp = new JScrollPane(jg_table);// �������Ϲ�����

        // JTable table = new JTable(dataModel);
        // JScrollPane scrollpane = new JScrollPane(table);

        jsp.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeTable(true);
            }
        });

        getContentPane().add(jsp, BorderLayout.CENTER);
        scrollPane.setViewportView(table);
    }
    public void resizeTable(boolean bool) {
        Dimension containerwidth = null;
        if (!bool) {
            //��ʼ��ʱ����������СΪ��ѡ��С��ʵ�ʴ�СΪ0
            containerwidth = jsp.getPreferredSize();
        } else {
            //������ʾ�������������С�ı䣬ʹ��ʵ�ʴ�С��������ѡ��С
            containerwidth = jsp.getSize();
        }
        //������������ getTable().
        int allwidth = jg_table.getIntercellSpacing().width;
        for (int j = 0; j < jg_table.getColumnCount(); j++) {
            //�����������Ŀ��
            int max = 0;
            for (int i = 0; i < jg_table.getRowCount(); i++) {
                int width = jg_table.getCellRenderer(i, j).getTableCellRendererComponent
                        (jg_table, jg_table.getValueAt(i, j), false,
                                false, i, j).getPreferredSize().width;
                if (width > max) {
                    max = width;
                }
            }
            //�����ͷ�Ŀ��
            int headerwidth = jg_table.getTableHeader().
                    getDefaultRenderer().getTableCellRendererComponent(
                    jg_table, jg_table.getColumnModel().
                            getColumn(j).getIdentifier(), false, false,
                    -1, j).getPreferredSize().width;
            //�п�����ӦΪ��ͷ���
            max += headerwidth;
            //�����п�
            jg_table.getColumnModel().
                    getColumn(j).setPreferredWidth(max);
            //�����������ȸ�ֵ���ǵ�Ҫ���ϵ�Ԫ��֮����������1������
            allwidth += max + jg_table.getIntercellSpacing().width;
        }
        allwidth += jg_table.getIntercellSpacing().width;
        //������ʵ�ʿ�ȴ�С�������Ŀ�ȣ�����Ҫ�����ֶ���Ӧ�������ñ������Ӧ
        if (allwidth > containerwidth.width) {
            jg_table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        } else {
            jg_table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
        }
    }


}