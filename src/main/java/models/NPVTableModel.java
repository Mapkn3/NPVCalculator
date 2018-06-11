package models;

import javax.swing.table.AbstractTableModel;
import java.util.Arrays;
import java.util.Vector;

public class NPVTableModel extends AbstractTableModel {
    public static final int PERIOD_COLUMN = 0;
    public static final int NPV_COLUMN = 6;

    private Vector<String> columnNames = new Vector<>(Arrays.asList(
            "Период",
            "Доходы, тыс. руб.",
            "Затраты, тыс. руб.",
            "Инвестиции, тыс. руб.",
            "Денежные потоки, тыс. руб.",
            "Дисконтированные денежные потоки, тыс. руб.",
            "Дисконтированные денежные потоки нарастающим итогом, тыс. руб."
    ));
    private Vector<Object []> data;
    private double discountRate;
    private int period;

    public NPVTableModel(double discount, double investments) {
        discountRate = discount;
        period = 0;
        data = new Vector<>();
        data.add(new Object[] {period, 0, 0, investments, investments, investments, investments});
    }

    public void addRow(double incomes, double expenses) {
        double lastCumulativeCashFlows = (double) data.get(period)[columnNames.size() - 1];
        period++;
        double cashFlows = incomes - expenses;
        double discountedCashFlows = cashFlows / Math.pow(1 + discountRate, period);
        double cumulativeCashFlows = lastCumulativeCashFlows + discountedCashFlows;
        data.add(new Object[] {
                period,
                incomes,
                expenses,
                0,
                cashFlows,
                discountedCashFlows,
                cumulativeCashFlows
        });
        fireTableDataChanged();
    }

    public double getNPV() {
        return (double) data.get(period)[columnNames.size() - 1];
    }

    public Vector<Double> getColumn(int index) {
        Vector<Double> values = new Vector<>();
        for (Object[] row : data) {
            Double value = Double.valueOf(row[index].toString());
            values.add(value);
        }
        return values;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.size();
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getValueAt(0, columnIndex).getClass();
    }
}

