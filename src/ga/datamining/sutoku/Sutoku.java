package ga.datamining.sutoku;

import java.awt.*;
import javax.swing.*;

public class Sutoku {

	static int size_width 	= 1400;
	static int size_height = 900;
	
	private static Sutoku sutoku = null;
	
	public static Sutoku get() {return sutoku==null?(sutoku = new Sutoku()):sutoku;}
	
	public static void main(String[] args) {
		// �D�{�����J

		// �إߵ���
		Sutoku.get().createWindow();
	}
	
	JFrame window = new JFrame();
	BoardRenderer boardRenderer  = new BoardRenderer();	// JPanel
	
	public void createWindow() {
		
		window.setSize(size_width, size_height);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// ������D�{���|�������
		window.setLocationRelativeTo(null);						// �����}�ҫ�|�b�ù�����
		
		// �[�J����
		window.add(boardRenderer);	// �ѽL
		//...	// �Ϫ�
		
		// ��ܵ���
		window.setVisible(true);
	}

}