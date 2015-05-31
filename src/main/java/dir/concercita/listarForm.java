package dir.concercita;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class listarForm extends JFrame {

	private JTable tabla;
	private Connection conex;
	private JButton cerrarButton;
	
	public listarForm(Connection con){
		this.conex = con;
		initForm();
		initComponents();
	}
	
	private void initForm(){
		setTitle("Listados");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);
	}
	
	private void initComponents(){
		
		JPanel cerrarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));		
		cerrarButton = new JButton("Cerrar");
		cerrarButton.setMnemonic('c');
		cerrarButton.setIcon(cargarIcono("dir/concercita/iconos/db cancel.gif"));
		cerrarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cerrarPanel.add(cerrarButton);
		
		tabla = new JTable(new CitasTablemodel(conex));
		JScrollPane s = new JScrollPane();
		s.setViewportView(tabla);
		
		getContentPane().add(s, BorderLayout.CENTER);
		getContentPane().add(cerrarPanel, BorderLayout.SOUTH);
				
	}
	
	private ImageIcon cargarIcono(String nombre){
		URL url = getClass().getClassLoader().getResource(nombre);
		ImageIcon icono = new ImageIcon(url);
		return icono;
	}
	
}
