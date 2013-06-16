import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

//import org.xml.sax.SAXParseException;

public class XmlOpenerEncrypter {
	private boolean state = false;
	private long time;
	private String key;
	private boolean mode;

	private String mark = "10";
	private String publicKey = "10";

	public XmlOpenerEncrypter(String k, boolean mode) {
		key = k;
		this.mode = mode;

	}
	public XmlOpenerEncrypter(String k, boolean mode, String PublicKey) {
		key = k;
		this.mode = mode;

	}

	public boolean go(String filePatchIn, String filePatchOut)
			throws IOException, ParserConfigurationException,
			TransformerException, SAXException {

		DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = dBuilder.parse(filePatchIn);
		// klucz, musi byc taki sam przy deszyfrowaniu
		// String k = "1a25s8fe5dsg65ad";
		long n = System.currentTimeMillis();

		try {

			NodeList nodeList = doc.getElementsByTagName("*");
			if (mode) {
				Element root = doc.getDocumentElement();
				root.setAttribute("Mark", mark);

			}
			for (int i = 0, len = nodeList.getLength(); i < len; i++) {
				Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					// Operacje na nodach xml:

					if (node.hasAttributes()) {

						// jesli sa atrybuty w nodzie - pobieram je;
						NamedNodeMap nodeMap = node.getAttributes();
						for (int l = 0; l < nodeMap.getLength(); l++) {
							Node nodeAtri = nodeMap.item(l);
							// zaszyfrowac nazwe atrybuty ?
							// byte[] enc =
							// AES.encrypt(nodeAtri.getNodeName().getBytes(),
							// k.getBytes());
							// System.out.println(new String(enc));
							// szyfruje atrybuty
							if (mode) {
								long start = System.currentTimeMillis();
								byte[] enc2 = AES.encrypt(nodeAtri
										.getTextContent().getBytes(), key
										.getBytes());
								long end = System.currentTimeMillis();
								time += (end - start);
								// nastepnie zamieniam zaszyfrowany string na
								// wartosci hexadecymalne w celu unikniecia
								// nieobslugiwanych znakow w xml
								String s = new String(AES.toHexString(enc2));
								nodeAtri.setNodeValue(new String(s));
							} else {
								long start = System.currentTimeMillis();
								byte[] enc2 = AES.decrypt(
										AES.toByteArray(nodeAtri
												.getTextContent()), key
												.getBytes());
								long end = System.currentTimeMillis();
								time += (end - start);
								nodeAtri.setTextContent(new String(enc2));
							}

						}
					}
					if ((node.getChildNodes().getLength()) < 2) {
						// szyfruje zawartosc miedzy tagami
						if (mode) {
							long start = System.currentTimeMillis();
							byte[] enc = AES.encrypt(node.getTextContent()
									.getBytes(), key.getBytes());
							long end = System.currentTimeMillis();
							time += (end - start);
							// zamiana
							String s = new String(AES.toHexString(enc));
							node.setTextContent(new String(s));
						} else {
							long start = System.currentTimeMillis();
							byte[] enc = AES.decrypt(
									AES.toByteArray(node.getTextContent()),
									key.getBytes());
							long end = System.currentTimeMillis();
							time += (end - start);
							node.setTextContent(new String(enc));
						}

					}

				}
			}
			// po zmodyfikowaniu tworze nowy plik xml o nazwie
			// nazwaEncrypted.xml
			String s = "10";
			if (!mode) {

				Element root2 = doc.getDocumentElement();
				s = root2.getAttribute("Mark");
				// System.out.println(mark);
				// byte[] enc = AES.decrypt(AES.toByteArray(mark),
				// key.getBytes());
				// mark = new String(enc);
				System.out.println("wew " + s);
				state = true;
				System.out.println("compare of strings: " + s.compareTo(mark));
			}

			if (s.compareTo(mark) == 0) {
				state = true;
				if (!mode) {
					Element root2 = doc.getDocumentElement();
					root2.removeAttributeNode(root2.getAttributeNode("Mark"));
				}
				TransformerFactory transformerFactory = TransformerFactory
						.newInstance();
				Transformer transformer = transformerFactory.newTransformer();
				DOMSource source = new DOMSource(doc);
				StreamResult result = new StreamResult(filePatchOut);
				transformer.transform(source, result);

			} else {
				System.out.println("zly klucz");
				state = false;
			}

			/*
			 * System.out.println("Done"); state = true; long m =
			 * System.currentTimeMillis(); long x= m-n;
			 * System.out.println("---------------------");
			 * System.out.println("czas pracy: "+x+"ms");
			 * System.out.println("czas szyfrowania :"+ time);
			 * System.out.println("---------------------");
			 */

		} catch (Exception e) {
			// e.printStackTrace();
			state = false;
		}

		return state;
	}

	public String getPublicKey() {
		return this.publicKey;
	}

}
