package dir.concercita;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class BotonesPanel extends JPanel {

	private JButton aceptarButton;
	private JButton cancelarButton;
	
	public BotonesPanel(){
		initPanel();
		initComponents();
	}
	
	private void initPanel(){
		this.setLayout(new FlowLayout(FlowLayout.RIGHT));
	}
	
	private void initComponents(){
		cancelarButton = new JButton("Cancelar", cargarIcono("dir/concercita/iconos/db cancel.gif"));
		cancelarButton.setMnemonic('C');
		cancelarButton.setIconTextGap(2);
		cancelarButton.setVerticalTextPosition(AbstractButton.CENTER);
		cancelarButton.setHorizontalTextPosition(AbstractButton.TRAILING);
		cancelarButton.setMargin(new Insets(2, 2, 2, 2));
		cancelarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelar(e);
			}
		});
		this.add(cancelarButton);
		
		aceptarButton = new JButton("Aceptar", cargarIcono("dir/concercita/iconos/ok.gif"));
		aceptarButton.setMnemonic('A');
		aceptarButton.setIconTextGap(2);
		aceptarButton.setVerticalTextPosition(AbstractButton.CENTER);
		aceptarButton.setHorizontalTextPosition(AbstractButton.TRAILING);
		aceptarButton.setMargin(new Insets(2, 2, 2, 2));
		aceptarButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceptar(e);
			}
		});
		this.add(aceptarButton);
	}
	
	private ImageIcon cargarIcono(String nombre){
		URL url = getClass().getClassLoader().getResource(nombre);
		ImageIcon icono = new ImageIcon(url);
		return icono;
	}
	
	public void cancelar(ActionEvent e){
		
		
		
		
	}
	
	public void aceptar(ActionEvent e){
				
	}
	
}
