package cmf.bus.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import javax.swing.JSeparator;
import java.awt.GridLayout;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.JTextArea;

import cmf.bus.Envelope;
import cmf.eventing.IEventBus;
import cmf.eventing.IEventHandler;
import cmf.examples.messages.EventTypeA;
import cmf.examples.messages.EventTypeB;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import com.jgoodies.forms.factories.FormFactory;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Map;

import javax.swing.JTextField;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class BusTester {

	public static final String EVENT_TYPE_A = "EventTypeA";
	public static final String EVENT_TYPE_B = "EventTypeB";
	
	private JFrame EventConsumerFrame;
	private JTextField reqmsgTextField;
	private final JTextArea logTextArea = new JTextArea();
	private IEventBus eventBus;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"app.config.xml"});

					BusTester window = context.getBean(BusTester.class);
					window.EventConsumerFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BusTester(IEventBus eventBus) {
		this.eventBus = eventBus;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		EventConsumerFrame = new JFrame();
		EventConsumerFrame.setTitle("Java Event Consumer & Producer");
		EventConsumerFrame.setBounds(100, 100, 1134, 513);
		EventConsumerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		EventConsumerFrame.setResizable(false);
		
		JPanel mainPanel = new JPanel();
		EventConsumerFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new GridLayout(2, 1, 2, 0));
		
	
		
		JPanel north = new JPanel();
		mainPanel.add(north);
		north.setLayout(new GridLayout(0, 3, 3, 0));
		
		JPanel eventSendingPanel = new JPanel();
		eventSendingPanel.setBorder(new TitledBorder(null, "Event Sending", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		north.add(eventSendingPanel);
		eventSendingPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel eventSendingPanelForm = new JPanel();
		eventSendingPanel.add(eventSendingPanelForm);
		eventSendingPanelForm.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblWhichTypeOf = DefaultComponentFactory.getInstance().createLabel("Which Type of Event?");
		eventSendingPanelForm.add(lblWhichTypeOf, "1, 1, right, top");
		
		final JComboBox eventComboBox = new JComboBox();
		eventComboBox.addItem("EventTypeA");
		eventComboBox.addItem("EventTypeB");
		eventSendingPanelForm.add(eventComboBox, "2, 1, fill, default");
		
		JLabel lblAddSomeEvent = DefaultComponentFactory.getInstance().createLabel("Add Some Event Text");
		eventSendingPanelForm.add(lblAddSomeEvent, "1, 2, default, top");
		
		final JTextPane eventTextPane = new JTextPane();
		eventSendingPanelForm.add(eventTextPane, "2, 2, fill, fill");
		
		JButton publishButton = new JButton("Publish");
		publishButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if( EVENT_TYPE_A.equals(eventComboBox.getSelectedItem()) ) {
					EventTypeA event = new EventTypeA();
					event.setMessage(eventTextPane.getText());
					
					try {
						eventBus.publish(event);
					}
					catch(Exception ex) {
						log("Failed to publish an event of type A: " + ex.toString());
					}
					
				} else if( EVENT_TYPE_B.equals(eventComboBox.getSelectedItem()))  {
					//TODO PUBLISH THIS TO THE BUS
					EventTypeB event = new EventTypeB();
					event.setMessage(eventTextPane.getText());
					
					try {
						eventBus.publish(event);
					}
					catch(Exception ex) {
						log("Failed to publish an event of type B: " + ex.toString());
					}
				}
			}
		});
		eventSendingPanelForm.add(publishButton, "2, 3, right, center");
		
		JPanel eventSubPanel = new JPanel();
		eventSubPanel.setBorder(new TitledBorder(null, "Event Subscriptions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		north.add(eventSubPanel);
		eventSubPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel eventSubForm = new JPanel();
		eventSubPanel.add(eventSubForm, BorderLayout.CENTER);
		eventSubForm.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblWhichEventTypes = DefaultComponentFactory.getInstance().createLabel("Which event types would you like to receive?");
		eventSubForm.add(lblWhichEventTypes, "1, 1");
		
		JCheckBox typeACheckBox = new JCheckBox("cmf.bus.events.EventTypeA");
		typeACheckBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				handleCheckBox(e);
			}
		});
		eventSubForm.add(typeACheckBox, "1, 2");
		
		JCheckBox typeBCheckBox = new JCheckBox("cmf.bus.events.EventTypeB");
		typeBCheckBox.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				handleCheckBox(e);	
			}
		});
		
		eventSubForm.add(typeBCheckBox, "1, 3");
		
		JLabel lblyouCantUnsubscribe = DefaultComponentFactory.getInstance().createLabel("(You can't unsubscribe)");
		eventSubForm.add(lblyouCantUnsubscribe, "1, 4");
		
		JPanel reqrespPanel = new JPanel();
		reqrespPanel.setBorder(new TitledBorder(null, "Request/Response", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		north.add(reqrespPanel);
		reqrespPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel reqrespForm = new JPanel();
		reqrespPanel.add(reqrespForm);
		reqrespForm.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblAddARequest = DefaultComponentFactory.getInstance().createLabel("Add a request message");
		reqrespForm.add(lblAddARequest, "1, 2, right, default");
		
		reqmsgTextField = new JTextField();
		reqrespForm.add(reqmsgTextField, "2, 2, fill, default");
		reqmsgTextField.setColumns(10);
		
		JButton requestButton = new JButton("Request");
		reqrespForm.add(requestButton, "2, 3");
		
		JPanel south = new JPanel();
		mainPanel.add(south);
		south.setLayout(new BorderLayout(0, 0));
		
		JPanel logPanel = new JPanel();
		logPanel.setBorder(new TitledBorder(null, "Received Events", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		south.add(logPanel);
		logPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		logPanel.add(panel, BorderLayout.CENTER);
		panel.setLayout(new GridLayout(2, 1, 0, 0));
		
		panel.add(this.logTextArea);
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1);
		
		JButton clearLogButton = new JButton("Clear Log");
		clearLogButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logTextArea.setText("");
			}
		});
		panel_1.add(clearLogButton);
	}
	
	private void handleCheckBox( ActionEvent e )  {
		AbstractButton button = (AbstractButton)e.getSource();
		if( button.getModel().isSelected() ) {
			if( "cmf.bus.events.EventTypeA".equals(button.getText())) {
				
				try {
					this.eventBus.subscribe(new IEventHandler<EventTypeA>() {

						public Class<EventTypeA> getEventType() {
							// TODO Auto-generated method stub
							return EventTypeA.class;
						}

						public Object handle(EventTypeA event,
								Map<String, String> headers) {
							log("Received an event of type A: " + event.getMessage());
							return null;
						}

						public Object handleFailed(Envelope envelope,
								Exception e) {
							log("Failed to receive an event of type A: " + e.toString());
							return null;
						}
						
					});
				}
				catch(Exception ex) {
					log("Failed to subscribe for events of type A: " + ex.toString());
				}
			} else if("cmf.bus.events.EventTypeB".equals(button.getText() )) {
				try {
					this.eventBus.subscribe(new IEventHandler<EventTypeB>() {

						public Class<EventTypeB> getEventType() {
							// TODO Auto-generated method stub
							return EventTypeB.class;
						}

						public Object handle(EventTypeB event,
								Map<String, String> headers) {
							log("Received an event of type B: " + event.getMessage());
							return null;
						}

						public Object handleFailed(Envelope envelope,
								Exception e) {
							log("Failed to receive an event of type B: " + e.getMessage());
							return null;
						}
						
					});
				}
				catch(Exception ex) {
					log("Failed to subscribe for events of type B: " + ex.toString());
				}
			}
		}
	}

	private void log(String message) {
		logTextArea.insert(message + "\n", 0);
	}
}
