/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ec.edu.monster.componentes;

import java.awt.*;
import javax.swing.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
/**
 *
 * @author rodri
 */
public class Estilos {
    
    public static void aplicarEstiloInput(JTextField campo) {
       // Fuente y colores base
        campo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        campo.setForeground(Color.BLACK);
        campo.setBackground(Color.WHITE);
        campo.setCaretColor(Color.BLACK);
        campo.setHorizontalAlignment(JTextField.LEFT);

        // 🔹 Borde inicial (más redondeado)
        Color colorBordeNormal = new Color(200, 200, 200);
        int radioBorde = 15; // entre más grande, más redondeado

        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorBordeNormal, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12) // padding
        ));

        // 🔹 Agregar efecto al enfocar/desenfocar
        campo.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(100, 149, 237), 2, true), // azul suave
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorBordeNormal, 1, true),
                        BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }
        
    public static void aplicarEstiloComboBox(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        combo.setForeground(Color.BLACK);
        combo.setBackground(Color.WHITE);
        combo.setFocusable(true);

        Color colorBordeNormal = new Color(200, 200, 200);
        Color colorBordeActivo = new Color(100, 149, 237); // azul suave



        // 🔹 Efecto al enfocar / perder foco
        combo.getEditor().getEditorComponent().addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                combo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorBordeActivo, 2, true),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                combo.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(colorBordeNormal, 1, true),
                        BorderFactory.createEmptyBorder(5, 10, 5, 10)
                ));
            }
        });

        // 🔹 Ajustar apariencia del botón desplegable
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

                if (isSelected) {
                    label.setBackground(new Color(43, 161, 240));
                    label.setForeground(Color.white);
                } else {
                    label.setBackground(Color.WHITE);
                    label.setForeground(Color.DARK_GRAY);
                }
                return label;
            }
        });
    }
    }
    

