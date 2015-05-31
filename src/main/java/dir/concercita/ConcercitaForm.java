package dir.concercita;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import dir.concercita.servicios.Citas;

public class ConcercitaForm extends JFrame implements Citas{

	private Connection conexion;
	private JButton insertarButton;
	private JButton modificarButton;
	private JButton eliminarButton;
	private JButton listarButton;
	private JPanel panel;
	protected static boolean modificado = false;
	
	public ConcercitaForm(){
		initForm();
		initComponents();
	}
	
	private void initForm(){
		setTitle("Citas");
		setSize(180, 220);
		setMinimumSize(this.getSize());
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				onWindowOpened();
			}
			public void windowClosing(WindowEvent e) {
				dispose();
			}
			public void windowClosed(WindowEvent e) {
				onWindowClosed();
			}
		});
	}
	
	private void initComponents(){
		insertarButton = new JButton("Insertar");
		insertarButton.setMnemonic('i');
		insertarButton.setIcon(cargarIcono("dir/concercita/iconos/plus.gif"));
		insertarButton.setIconTextGap(5);
		insertarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				insertar();
			}
		});
		
		modificarButton = new JButton("Modificar");
		modificarButton.setMnemonic('m');
		modificarButton.setIcon(cargarIcono("dir/concercita/iconos/edit-3.gif"));
		modificarButton.setIconTextGap(5);
		modificarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				modificar();
			}
		});
		
		eliminarButton = new JButton("Eliminar");
		eliminarButton.setMnemonic('e');
		eliminarButton.setIcon(cargarIcono("dir/concercita/iconos/delete.gif"));
		eliminarButton.setIconTextGap(5);
		eliminarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eliminar();
			}
		});
		
		listarButton = new JButton("Listar");
		listarButton.setMnemonic('l');
		listarButton.setIcon(cargarIcono("dir/concercita/iconos/treeview.gif"));
		listarButton.setIconTextGap(5);
		listarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listar();
			}
		});
		
		panel = new JPanel(new GridBagLayout());
		panel.setBorder(new TitledBorder("Acciones"));
		GridBagConstraints c = new GridBagConstraints();
		
		c.anchor = GridBagConstraints.CENTER;
		c.fill = GridBagConstraints.BOTH;
		c.gridheight = 1;
		c.gridwidth = 1;
		c.weightx = 0.0;
		c.weighty = 0.0;
		c.insets = new Insets(5,5,5,5);
		c.ipadx = 0;
		c.ipady = 0;
		c.gridx = 0;
		
		c.gridy = 0;
		panel.add(insertarButton, c);
		
		c.gridy = 1;
		panel.add(modificarButton, c);
		
		c.gridy = 2;
		panel.add(eliminarButton, c);
		
		c.gridy = 3;
		panel.add(listarButton, c);
				
		JPanel pan = new JPanel(new BorderLayout());
		pan.add(panel, BorderLayout.NORTH);
				
		getContentPane().add(pan, BorderLayout.CENTER);
		
	}
	
	private void onWindowOpened(){
		try {
			//Cargar el driver JDBC para sqlServer.
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			//Establecemos la cadena de conexion
			String url = "jdbc:sqlserver://localhost:1433;databaseName=concercita";
			//Conectmos a la  base de datos a traves de la Connection.
			conexion = DriverManager.getConnection(url, "sa", "administrador");
		} catch (ClassNotFoundException e1) {
			JOptionPane.showMessageDialog(this, "No se encuentra el Driver JDBC", "Error de driver",
					JOptionPane.ERROR_MESSAGE);
		} catch (SQLException e2){
			JOptionPane.showMessageDialog(this, "No se puede conectar con la base de datos " + e2.toString(), 
					"Error de conexión", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void onWindowClosed(){
		try{
			conexion.close();
		}catch(SQLException e4){
			JOptionPane.showMessageDialog(this, "No se pudo cerrar la conexión", 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	@Override
	public void insertar() {
		InsertarDialog i = new InsertarDialog(conexion);
		i.setVisible(true);
	}

	@Override
	public void modificar() {
		//Preguntamos por el identificador a modificar.
		String id = JOptionPane.showInputDialog(this, "Introduzca identificador del registro a modificar");
		if (id != null){
			//Si no es nulo consultamos la base de datos a ver si existe.
			String consulta = "select * from citas where id_cita=" + id;
			try {
				Statement sentencia = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, 
						ResultSet.CONCUR_READ_ONLY);
				//Ejecutamos la consulta de selección y recuperamos los datos del registro.
				ResultSet resultado = sentencia.executeQuery(consulta);
				//colocamos el puntero antes del primer registro.
				resultado.beforeFirst();
				if (resultado.next()){
					//Si el registro no está vacio instanciamos el formulario de modificación
					//pasándole como parametros la conexión y el ResultSet apuntando ya al primer y único registro.
					ModificarDialog m = new ModificarDialog(conexion, resultado);
					m.setVisible(true);
					//Si realmente se modificó la cita se mostrará el mensaje de confirmación.
					if (modificado){
						JOptionPane.showMessageDialog(this, "La cita se modificó correctamente", 
								"Modificar Registro", JOptionPane.INFORMATION_MESSAGE);
						modificado = false;
					}
				}else{
					JOptionPane.showMessageDialog(this, "El identificador no existe", 
							"Modificar Registro", JOptionPane.WARNING_MESSAGE);
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, "No se pudo modificar el registro. " + '\n' +
						e1.toString() , "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	@Override
	public void eliminar() {
		String id = JOptionPane.showInputDialog(this, "Introduzca identificador del registro a eliminar");
		if (id != null){
			String consulta = "delete from citas where id_cita=" + id;
			try { 
				Statement sentencia = conexion.createStatement();
				if (sentencia.executeUpdate(consulta) > 0){
					JOptionPane.showMessageDialog(this, "La cita se eliminó correctamente", 
							"Eliminar Registro", JOptionPane.INFORMATION_MESSAGE);
				}else{
					JOptionPane.showMessageDialog(this, "El identificador no existe", 
							"Eliminar Registro", JOptionPane.WARNING_MESSAGE);
				}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(this, "No se pudo eliminar la cita." + "\n" 
						+ e1.toString(), "Error", JOptionPane.ERROR_MESSAGE);
				e1.printStackTrace();
			}
		}
		
	}

	@Override
	public void listar() {
		listarForm f = new listarForm(conexion);
		f.setVisible(true);
	}
	
	private ImageIcon cargarIcono(String nombre){
		URL url = getClass().getClassLoader().getResource(nombre);
		ImageIcon icono = new ImageIcon(url);
		return icono;
	}

}
