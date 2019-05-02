/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.sun.speech.freetts.*;

public class Gui extends JFrame implements ActionListener, KeyListener{
	//KHAI BÁO CÁC BIẾN 
	private static final String VOICENAME="kevin16";
	VoiceManager voiceManager=VoiceManager.getInstance();
	
	private String doctu;
	private ImageIcon image;
	private JTextArea tx;
	private JLabel lblImage;
	int n=0;
	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JTextField tfNhap;
	private JPanel p4;
	private JButton btDich, btAnhViet, btThemTu;
	private String data;
	WikiSearch translatorEV;

	public Gui() throws MalformedURLException,IOException, ParseException, org.json.simple.parser.ParseException {
		setSize(750, 655);
		setTitle("Tu dien bach khoa toan thu");
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		BuidingGUi();

	}

	private void BuidingGUi()throws  IOException, ParseException, org.json.simple.parser.ParseException {
		//Dich Anh Viet Google translate 
		translatorEV = new WikiSearch();	        
		JPanel p_main = new JPanel();
		DictEV.HasdMapEVOn();
		//TẠO CÁC PANEL P1,P2,P3,P4 TƯƠNG ƯỚNG VỚI NHẬP, TÙY CHỌN, DANH SÁCH TỪ, NGHĨA
		// P1: NHẬP
		JPanel p1 = new JPanel();
		JLabel lbNhap = new JLabel("Nhập:");
		tfNhap = new JTextField(15);
		btDich = new JButton("Dịch");
		p1.setBorder(BorderFactory.createTitledBorder("Tìm từ"));
		p1.add(lbNhap, new BorderLayout().SOUTH);
		p1.add(tfNhap, new BorderLayout().SOUTH);
		p1.add(btDich, new BorderLayout().SOUTH);
		p1.setPreferredSize(new Dimension(300, 70));
		tfNhap.addKeyListener(this);
		// P2: TÙY CHỌN
		JPanel p2 = new JPanel();
		btAnhViet = new JButton("Anh Việt");
                btThemTu = new JButton("Them Tu");
		p2.setBorder(BorderFactory.createTitledBorder("Tùy chọn"));
		p2.add(btAnhViet, new BorderLayout().SOUTH);
                p2.add(btThemTu, new BorderLayout().SOUTH);
		p2.setPreferredSize(new Dimension(400, 70));

		// P3: DANH SÁCH TỪ
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.setBorder(BorderFactory.createTitledBorder("Danh sách từ"));
		p3.setPreferredSize(new Dimension(300, 530));
		
		listModel=new DefaultListModel<>();
		list=new JList<>(listModel);
		list.setBorder(BorderFactory.createTitledBorder(""));
		p3.add(new JScrollPane(list),BorderLayout.CENTER);
		
		ReloadJListEV();
		 
		// P4: NGHĨA CỦA TỪ
		p4 = new JPanel(new BorderLayout());
		tx = new JTextArea(44, 33);
                tx.setLineWrap(true);
                tx.setWrapStyleWord(true);
		p4.add(tx);	
		
		ImageIcon image=new ImageIcon("img/speaker.jpg");
		JLabel lblSpeaker=new JLabel(image,JLabel.LEFT);
		lblSpeaker.addMouseListener(new MouseAdapter() {
			 @Override
			  public void mouseClicked(MouseEvent e) {
				 	Voice voice;
					voice=voiceManager.getVoice(VOICENAME);
					voice.allocate();
					
					try{				
							voice.speak(doctu);			
					}
					catch(Exception e1){
						
					}
			  }
		});
		p4.add(lblSpeaker,BorderLayout.NORTH);
		p4.setBorder(BorderFactory.createTitledBorder("Nghĩa của từ"));
		p4.setPreferredSize(new Dimension(400, 530));
		String kq="null";
		
		DoiHinhMoTa(kq);
		p4.setBackground(Color.white);
		//Thêm các panel vào panel chính
		p_main.add(p1);
		p_main.add(p2);
		p_main.add(p3);
		p_main.add(p4);
		
		// THÊM CÁC  ACTIONLISTIONER
		btDich.addActionListener(this);
		btAnhViet.addActionListener(this);
                //Hành động click vào " Danh sách từ "
		list.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				
				 JList source = (JList) e.getSource();
				
				    String word = (String) source.getSelectedValue();
				    if(n==0){
				    	String resulf=DictEV.hMapEV.get(word);   
				    	if(resulf!=null){
					    	String[] kq= resulf.split("#");
					    	tx.setText(kq[0]+"\n\t"+kq[1]+"\n\t"+kq[2]);
							tfNhap.setText(word);
							Xoahinh();
							if(kq[3]!=null)
							{
								DoiHinhMoTa(kq[3]);
							}	
				    	}
				    	
				    }
			}

		});

		this.add(p_main);
		
	}

	public static void main(String[] args) throws MalformedURLException, IOException, ParseException, org.json.simple.parser.ParseException {
		new Gui().setVisible(true);
		DictEV.HasdMapEVOn();
	}
	
	//CÁC HÀM 
	private void Xoahinh() {		
		p4.remove(lblImage);
	}

	private void DoiHinhMoTa(String kq){	
		image=new ImageIcon(kq);
		lblImage=new JLabel(image);
		lblImage.setPreferredSize(new Dimension(500,300));
		p4.add(lblImage,BorderLayout.SOUTH);
	}
	//CÁC HÀNH ĐỘNG (EVEN)
	//ReloadJList
	private void ReloadJListEV() {
		listModel.clear();
		for (String i : DictEV.word) {
			 listModel.addElement(i);
	        }
	}
	//Actionlistener
	@Override
	public void actionPerformed(ActionEvent e) {
            Object o = e.getSource();
		
            if(o.equals(btAnhViet)){
                n=0;
                ReloadJListEV();	
            }
		
            //Dich tieng Anh->Viet
            if (o.equals(btDich) && n==0) {
                String resulf = DictEV.hMapEV.get(tfNhap.getText());
			
                if (resulf != null) {
                    String[] kq= resulf.split("#");
                    tx.setText(kq[0]+"\n\t"+kq[1]+"\n\t"+kq[2]);
                    Xoahinh();
                    DoiHinhMoTa(kq[3]);
                } else {
                    //Hiện thị JOptionPane
                    int resuf= JOptionPane.showConfirmDialog(null, " Bạn có cần trợ giúp từ Wikimedia không", "TỪ KHÔNG CÓ TRONG TỪ ĐIỂN", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                    if (resuf==JOptionPane.YES_NO_CANCEL_OPTION){		
                    tx.setText("Từ bạn tìm không có trong từ điển.");
    		}
		else{
                    try {
			data = translatorEV.translate(tfNhap.getText());
                    } catch (IOException | org.json.simple.parser.ParseException e1) {
                        e1.printStackTrace();
                    }
                    tx.setText(data);
                }
            }		
        }
    }
		// Hành động nhập vào khung "Nhập" từ của từ điển
    @Override
    public void keyPressed(KeyEvent arg0) {
		
		
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        if(n==0){
            String txt = tfNhap.getText();
            listModel.clear();
            for(int i = 0; i < DictEV.word.size(); i ++){
            if(DictEV.word.get(i).startsWith(txt)){
                listModel.addElement(DictEV.word.get(i));
                }
            }
        }	
    }

    @Override
    public void keyTyped(KeyEvent arg0) {		
    }

}