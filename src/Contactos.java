package Contactos;
import Contactos.Persona;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.NumberFormatException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Contactos extends JFrame implements ActionListener {
    private JTable tabla1;
    private DefaultTableModel model;
    private Container contenedor;
    private JLabel nombre, telefono;
    private JTextField campoNombre, campoTelefono;
    private JButton agregar, mostrar, actualizar, eliminar;
    private JPanel Contactos;
    private JScrollPane scrollTabla;

    public Contactos() {
        inicio();
        setTitle("Contactos");
        setSize(270, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
    }

    private void inicio() {
        contenedor = getContentPane();
        contenedor.setLayout(null);
        nombre = new JLabel();
        nombre.setText("Nombre:");
        nombre.setBounds(20, 20, 135, 23);
        campoNombre = new JTextField();
        campoNombre.setBounds(105, 20, 135, 23);
        telefono = new JLabel();
        telefono.setText("Teléfono:");
        telefono.setBounds(20, 80, 135, 23);
        campoTelefono = new JTextField();
        campoTelefono.setBounds(105, 80, 135, 23);
        agregar = new JButton();
        agregar.setText("Añadir");
        agregar.setBounds(20, 150, 80, 23);
        agregar.addActionListener(this);
        eliminar = new JButton();
        eliminar.setText("Eliminar");
        eliminar.setBounds(20, 280, 80, 23);
        eliminar.addActionListener(this);
        actualizar = new JButton();
        actualizar.setText("Actualizar");
        actualizar.setBounds(120, 280, 120, 23);
        actualizar.addActionListener(this);
        mostrar = new JButton();
        mostrar.setText("Mostrar");
        mostrar.setBounds(120, 150, 120, 23);
        mostrar.addActionListener(this);
        tabla1 = new JTable();
        model = new DefaultTableModel();
        scrollTabla = new JScrollPane(tabla1);
        scrollTabla.setBounds(20, 190 ,220, 80);
        scrollTabla.setViewportView(tabla1);
        contenedor.add(nombre);
        contenedor.add(telefono);
        contenedor.add(campoNombre);
        contenedor.add(campoNombre);
        contenedor.add(campoTelefono);
        contenedor.add(campoTelefono);
        contenedor.add(agregar);
        contenedor.add(eliminar);
        contenedor.add(actualizar);
        contenedor.add(mostrar);
        contenedor.add(scrollTabla);
    }

    @Override
    public void actionPerformed(ActionEvent evento) {
        if (evento.getSource() == agregar) {
            agregarDatos();
        }
        if (evento.getSource() == mostrar) {
            mostrarDatos();
        }
        if (evento.getSource() == actualizar) {
            actualizarDatos();
        }
        if (evento.getSource() == eliminar) {
            borrarDatos();
        }
    }
    File file = new File("contactos.txt");
    public void agregarDatos() {
        try {
            String nombreNumeroString, nombre1, nuevoNombre;
            long numero1;
            int index;
            nuevoNombre = campoNombre.getText();
            long nuevoNumero = Long.parseLong(campoTelefono.getText());

            File file = new File("contactos.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            boolean found = false;
            while (raf.getFilePointer() < raf.length()) {
                nombreNumeroString = raf.readLine();
                String[] lineSplit = nombreNumeroString.split(",");
                nombre1 = lineSplit[0];
                numero1 = Long.parseLong(lineSplit[1]);
                if (nombre1 == nuevoNombre || numero1 == nuevoNumero) {
                    found = true;
                    break;
                }
            }
            if (found == false) {
                nombreNumeroString = nuevoNombre + "," + nuevoNumero;
                raf.writeBytes(nombreNumeroString);
                raf.writeBytes(System.lineSeparator());
                raf.close();
            } else {
                raf.close();
            }
        } catch (IOException ex) {
            System.out.println();
        } catch (NumberFormatException nef) {
            System.out.println();
        }
    }

    public void mostrarDatos() {
        String titulo = "Nombre,Telefono";
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            DefaultTableModel model = (DefaultTableModel) tabla1.getModel();
            model.getDataVector().removeAllElements();
            model.fireTableDataChanged();
            String[] splitTitle = titulo.trim().split(",");
            model.setColumnIdentifiers(splitTitle);

            Object[] object = reader.lines().toArray();
            for (int i = 0; i < object.length; i++) {
                String[] separar = object[i].toString().trim().split(",");
                model.addRow(separar);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Contactos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarDatos() {
        try {
            String nombre1, nuevoNombre, nombreNumeroString;
            long numero1;
            int index;
            nuevoNombre = campoNombre.getText();
            long nuevoNumero = Long.parseLong(campoTelefono.getText());

            File file = new File("contactos.txt");

            if (!file.exists()) {
                file.createNewFile();
            }

            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            boolean found = false;

            while (raf.getFilePointer() < raf.length()) {
                nombreNumeroString = raf.readLine();
                String[] lineSplit = nombreNumeroString.split(",");
                nombre1 = lineSplit[0];
                numero1 = Long.parseLong(lineSplit[1]);
                if (nombre1 == nuevoNombre || numero1 == nuevoNumero) {
                    found = true;
                    break;
                }
            }
            if (found == true) {
                File tmpFile = new File("temp.txt");
                RandomAccessFile tmpraf = new RandomAccessFile(tmpFile, "rw");
                raf.seek(0);

                while (raf.getFilePointer() < raf.length()) {
                    nombreNumeroString = raf.readLine();
                    index = nombreNumeroString.indexOf(',');
                    nombre1 = nombreNumeroString.substring(0, index);

                    if (nombre1.equals(nuevoNombre)) {
                        nombreNumeroString = nombre1 + "," + String.valueOf(nuevoNumero);
                    }
                    tmpraf.writeBytes(nombreNumeroString);
                    tmpraf.writeBytes(System.lineSeparator());
                }
                raf.seek(0);
                tmpraf.seek(0);
                while (tmpraf.getFilePointer() < tmpraf.length()) {
                    raf.writeBytes(tmpraf.readLine());
                    raf.writeBytes(System.lineSeparator());
                }
                raf.setLength(tmpraf.length());
                tmpraf.close();
                raf.close();
                tmpFile.delete();
            } else {
                raf.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Contactos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException nef) {
        }
    }

    public void borrarDatos() {
        try {
            String nombreNumeroString, nombre1, nuevoNombre;
            long numero1;
            int index;
            nuevoNombre = campoNombre.getText();

            File file = new File("contactos.txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            boolean found = false;

            while (raf.getFilePointer() < raf.length()) {

                nombreNumeroString = raf.readLine();

                String[] lineSplit = nombreNumeroString.split(",");

                nombre1 = lineSplit[0];
                numero1 = Long.parseLong(lineSplit[1]);

                if (nombre1 == nuevoNombre) {
                    found = true;
                    break;
                }
            }

            if (found == true) {

                File tmpFile = new File("temp.txt");

                RandomAccessFile tmpraf = new RandomAccessFile(tmpFile, "rw");

                raf.seek(0);

                while (raf.getFilePointer() < raf.length()) {

                    nombreNumeroString = raf.readLine();

                    index = nombreNumeroString.indexOf(',');
                    nombre1 = nombreNumeroString.substring(0, index);

                    if (nombre1.equals(nuevoNombre)) {
                        continue;
                    }
                    tmpraf.writeBytes(nombreNumeroString);
                    tmpraf.writeBytes(System.lineSeparator());
                }
                raf.seek(0);
                tmpraf.seek(0);
                while (tmpraf.getFilePointer() < tmpraf.length()) {
                    raf.writeBytes(tmpraf.readLine());
                    raf.writeBytes(System.lineSeparator());
                }
                raf.setLength(tmpraf.length());
                tmpraf.close();
                raf.close();
                tmpFile.delete();
            } else {
                raf.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(Contactos.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException nef) {
        }
    }
}
