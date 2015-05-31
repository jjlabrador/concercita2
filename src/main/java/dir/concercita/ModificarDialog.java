package dir.concercita;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ModificarDialog extends JDialog {

	private Connection conexion;
	private ResultSet fila;
	private JTextField nombreText;
	private JTextField fechaText;
	private JTextArea asuntoText;
	private JButton aceptarButton;
	private JButton cancelarButton;
	private int registro;
	
	public ModificarDialog(Connection conex, ResultSet result) {
		this.fila = result;
		this.conexion = conex;
		initDialog();
		initComponents();
		cargarRegistro();
	}
	
	private void initDialog(){
		setTitle("Modificar cita");
		setSize(300, 300);
		setMinimumSize(this.getSize());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setModal(true);
		setLocationRelativeTo(null);
	}
	
	private void initComponents(){
		Insets margen = new Insets(5, 5, 5, 5);
		JPanel panel = new JPanel(new GridBagLayout());
		
		JLabel nombreLabel = new JLabel("Nombre:");
		panel.add(nombreLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, margen, 0, 0));		
		
		JLabel fechaLabel = new JLabel("Fecha:");
		panel.add(fechaLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, margen, 0, 0));		
		
		JLabel asuntoLabel = new JLabel("A:");
		panel.add(asuntoLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHEAST, GridBagConstraints.NONE, margen, 0, 0));		
		
		nombreText = new JTextField();
		nombreText.setPreferredSize(new Dimension(150, 20));
		panel.add(nombreText, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, margen, 0, 0));		
		
		fechaText = new JTextField();
		fechaText.setPreferredSize(new Dimension(80, 20));
		panel.add(fechaText, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 
				GridBagConstraints.NORTHWEST, GridBagConstraints.VERTICAL, margen, 0, 0));		
		
		asuntoText = new JTextArea();
		asuntoText.setWrapStyleWord(true);
		asuntoText.setLineWrap(true);
		
		JScrollPane scrollPanel = new JScrollPane(asuntoText);
		panel.add(scrollPanel, new GridBagConstraints(1, 2, 1, 1, 1.0, 1.0, 
				GridBagConstraints.WEST, GridBagConstraints.BOTH, margen, 0, 0));		
		
		aceptarButton = new JButton("Aceptar");
		aceptarButton.setIcon(cargarIcono("dir/concercita/iconos/ok.gif"));
		aceptarButton.setMnemonic('a');
		aceptarButton.setIconTextGap(2);
		aceptarButton.setMargin(new Insets(2,2,2,2));
		aceptarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modificarRegistro();
			}
		});
				
		cancelarButton = new JButton("Cancelar");
		cancelarButton.setMnemonic('c');
		cancelarButton.setIcon(cargarIcono("dir/concercita/iconos/db cancel.gif"));
		cancelarButton.setIconTextGap(2);
		cancelarButton.setMargin(new Insets(2,2,2,2));
		cancelarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConcercitaForm.modificado = false;
				dispose();
			}
		});
		
		JPanel botonesPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		botonesPanel.add(aceptarButton);
		botonesPanel.add(cancelarButton);
				
		getContentPane().add(botonesPanel, BorderLayout.SOUTH);
		getContentPane().add(panel, BorderLayout.CENTER);
	}
	
	private void cargarRegistro(){
		
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		try {
			this.registro = fila.getInt("Id_cita");
			nombreText.setText(fila.getObject("nombre").toString());
			fechaText.setText(formato.format(fila.getObject("fecha")));
			asuntoText.setText(fila.getObject("asunto").toString());
		} catch (SQLException e) {
			System.out.println(e.toString());
		}
	}
	
	private void modificarRegistro(){
		Statement sentencia;
		String consulta = "update citas set nombre = '" + nombreText.getText() + 
			"', fecha = '" + fechaText.getText() + "', asunto = '" + asuntoText.getText() +
			"' where Id_cita = " + registro;
		try {
			sentencia = conexion.createStatement();
			sentencia.executeUpdate(consulta);
			//Esgtablecemos el atributo de ConcercitaForm a true para que muestre el mensaje de confirmación.
			ConcercitaForm.modificado = true;
			dispose();
		} catch (SQLException e1) {
			JOptionPane.showMessageDialog(this, "No se puede modificar la cita" + '\n', 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
		
	private ImageIcon cargarIcono(String nombre){
		URL url = getClass().getClassLoader().getResource(nombre);
		ImageIcon icono = new ImageIcon(url);
		return icono;
	}
}
