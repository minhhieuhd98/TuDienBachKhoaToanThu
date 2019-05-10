/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;



import static dictionary.Dict.word;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;

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

public class Gui extends JFrame implements ActionListener, KeyListener{
	//KHAI BÁO CÁC BIẾN 
	
	private String doctu;
	private ImageIcon image;
	private JTextArea tx;
	private JLabel lblImage;
	private DefaultListModel<String> listModel;
	private JList<String> list;
	private JTextField tfNhap;
	private JPanel p4;
	private JButton btSearch, btReset, btThemTu, btHistory;
	private String data;
	WikiSearch wikiSearch;

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
		wikiSearch = new WikiSearch();	        
		JPanel p_main = new JPanel();
		Dict.HasdMapEVOn();
		//TẠO CÁC PANEL P1,P2,P3,P4 TƯƠNG ƯỚNG VỚI NHẬP, TÙY CHỌN, DANH SÁCH TỪ, NGHĨA
		// P1: NHẬP
		JPanel p1 = new JPanel();
		JLabel lbNhap = new JLabel("Nhập:");
		tfNhap = new JTextField(15);
		btSearch = new JButton("Search");

		p1.setBorder(BorderFactory.createTitledBorder("Tìm từ"));
		p1.add(lbNhap, new BorderLayout().SOUTH);
		p1.add(tfNhap, new BorderLayout().SOUTH);
		p1.add(btSearch, new BorderLayout().SOUTH);
		p1.setPreferredSize(new Dimension(300, 70));
		tfNhap.addKeyListener(this);
		// P2: TÙY CHỌN
		JPanel p2 = new JPanel();
		btReset = new JButton("Reset");
                btThemTu = new JButton("Add Word");
                btHistory = new JButton("History");
		p2.setBorder(BorderFactory.createTitledBorder("Tùy chọn"));
		p2.add(btReset, new BorderLayout().SOUTH);
                p2.add(btThemTu, new BorderLayout().SOUTH);
                p2.add(btHistory, new BorderLayout().SOUTH);
		p2.setPreferredSize(new Dimension(400, 80));

		// P3: DANH SÁCH TỪ
		JPanel p3 = new JPanel();
		p3.setLayout(new BorderLayout());
		p3.setBorder(BorderFactory.createTitledBorder("Danh sách từ"));
		p3.setPreferredSize(new Dimension(300, 530));
		
		listModel=new DefaultListModel<>();
		list=new JList<>(listModel);
		list.setBorder(BorderFactory.createTitledBorder(""));
		p3.add(new JScrollPane(list),BorderLayout.CENTER);
		
		ReloadJListWiki();
		 
		// P4: NGHĨA CỦA TỪ
		p4 = new JPanel(new BorderLayout());
		tx = new JTextArea(44, 33);
                tx.setLineWrap(true);
                tx.setWrapStyleWord(true);
		p4.add(tx);	
		
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
		
		btSearch.addActionListener(this);
                
                btThemTu.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btAddActionPerformed(e);
                    }
                });
                
                btHistory.addActionListener(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        btHistoryAction(e);
                    }

                    private void btHistoryAction(ActionEvent e) {
                        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                    }
                    
                });
                
                btReset.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        resetList(e);
                    }

                    private void resetList(ActionEvent e) {
                         ReloadJListWiki();
                    }
                });
                
		list.addListSelectionListener(new ListSelectionListener() {
			
		@Override
		public void valueChanged(ListSelectionEvent e) {
				
                    JList source = (JList) e.getSource();
				
                    String word = (String) source.getSelectedValue();
                        
                    tfNhap.setText(word);
                    String in = Dict.search(word);
                    tx.setText(in);
                    }
                });

		this.add(p_main);
		
	}

	public static void main(String[] args) throws MalformedURLException, IOException, ParseException, org.json.simple.parser.ParseException {
		new Gui().setVisible(true);
		Dict.HasdMapEVOn();
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
	private void ReloadJListWiki() {
		listModel.clear();
		for (String i : Dict.word) {
			 listModel.addElement(i);
	        }
	}
	//Actionlistener
        
        public void btAddActionPerformed(ActionEvent e){
            ThemTu addWord = new ThemTu();
            addWord.show();
        }
        
	@Override
	public void actionPerformed(ActionEvent e) {
            Object o = e.getSource();
		
            
            ReloadJListWiki();	
            
		
            if (o.equals(btSearch)) {
                String resulf = Dict.hMapEV.get(tfNhap.getText());
		
                System.out.println(resulf);
                if (resulf != null) {
                    String[] kq= resulf.split("#");
                    tx.setText(kq[0]+"\n\t"+kq[1]);
                    Xoahinh();
                    DoiHinhMoTa(kq[1]);
                } else {
                    int resuf= JOptionPane.showConfirmDialog(null, " Bạn có cần trợ giúp từ Wikimedia không", "TỪ KHÔNG CÓ TRONG TỪ ĐIỂN", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
                    if (resuf==JOptionPane.YES_NO_CANCEL_OPTION){		
                    tx.setText("Từ bạn tìm không có trong từ điển.");
    		}
		else{
                    try {
			data =  wikiSearch.translate(tfNhap.getText());
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
            String txt = tfNhap.getText();
            listModel.clear();
            for(int i = 0; i < Dict.word.size(); i ++){
            if(Dict.word.get(i).startsWith(txt)){
                listModel.addElement(Dict.word.get(i));
                }
            }	
    }

    @Override
    public void keyTyped(KeyEvent arg0) {		
    }

}