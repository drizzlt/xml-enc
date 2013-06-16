import java.awt.EventQueue;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.swing.JFrame;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public class mainX {

	public static void main(String[] args) throws IOException,
			ParserConfigurationException, TransformerException, SAXException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			ShortBufferException, IllegalBlockSizeException,
			BadPaddingException {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				create();
			}
		});

		// key: = 1a25s8fe5dsg65ad
		// syso;

	}

	private static void create() {
		// TODO making application
		window w = new window();
		w.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		w.setLocationRelativeTo(null);
		w.setSize(500, 250);
		w.setResizable(false);
		w.setVisible(true);

	}

}
