/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 *
 * @author Farmacia-Ingeniero
 */
public class ComboBoxTableCellRenderer extends DefaultCellEditor {

    private JComboBox<String> comboBox;
    private Map<String, String> valueToIdMap;

    public ComboBoxTableCellRenderer(Map<String, String> valueToIdMap) {
        super(new JComboBox<>(valueToIdMap.keySet().toArray(new String[0])));
        this.valueToIdMap = valueToIdMap;
        comboBox = (JComboBox<String>) editorComponent;
        comboBox.setRenderer(new DefaultListCellRenderer());
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (value != null) {
            value = valueToIdMap.get(value.toString());
        }
        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }
}
