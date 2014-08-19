package org.bloblines.ui.rich;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ServerPanel extends JPanel {

	/** serialUID */
	private static final long serialVersionUID = -2119065506456317931L;

	private JTextField txtLogin;
	private JLabel lblStatusIndicator;
	private JButton btnStartServer;
	private JButton btnStopServer;
	private JButton btnConnect;

	private Bloblines client;

	/**
	 * Create the panel.
	 */
	public ServerPanel(Bloblines pClient) {
		super();
		this.client = pClient;
		setPreferredSize(new Dimension(400, 400));
		setMinimumSize(new Dimension(400, 400));
		setMaximumSize(new Dimension(400, 400));

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		JPanel panelStatus = new JPanel();
		add(panelStatus);

		JLabel lblStatus = new JLabel("Server Status : ");
		panelStatus.add(lblStatus);

		lblStatusIndicator = new JLabel();
		panelStatus.add(lblStatusIndicator);

		JPanel panelControl = new JPanel();
		add(panelControl);

		btnStartServer = new JButton("Start");
		btnStartServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (((JButton) e.getSource()).isEnabled()) {
					client.startServer();
					refresh();
				}
			}
		});
		panelControl.add(btnStartServer);

		btnStopServer = new JButton("Stop");
		btnStopServer.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (((JButton) e.getSource()).isEnabled()) {
					client.stopServer();
					refresh();
				}
			}
		});
		panelControl.add(btnStopServer);

		JPanel panelLogin = new JPanel();
		add(panelLogin);

		JLabel lblLogin = new JLabel("Login : ");
		panelLogin.add(lblLogin);

		txtLogin = new JTextField();
		txtLogin.setColumns(20);
		panelLogin.add(txtLogin);

		btnConnect = new JButton("Connect");
		btnConnect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (((JButton) e.getSource()).isEnabled()) {
					client.connect(txtLogin.getText());
				}
			}
		});
		add(btnConnect);

		// Initialize components behavior
		refresh();
	}

	public void refresh() {
		boolean serverStarted = client.isServerStarted();
		lblStatusIndicator.setText(serverStarted ? "ON" : "OFF");
		btnStartServer.setEnabled(!serverStarted);
		btnStopServer.setEnabled(serverStarted);
		txtLogin.setEnabled(serverStarted);
		btnConnect.setEnabled(serverStarted);
	}
}
