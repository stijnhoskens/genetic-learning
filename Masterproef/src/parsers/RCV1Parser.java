package parsers;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import text.SentenceProcessing;

public class RCV1Parser {

	private Writer writer;
	private int count;

	public static void main(String[] args) throws Exception {
		Path input = Paths.get("C:\\Users\\User\\Documents\\School\\"
				+ "Masterproef\\test\\rcv1");
		Path output = Paths.get("C:\\Users\\User\\Documents\\School\\"
				+ "Masterproef\\test\\rcv2.txt");
		new RCV1Parser().parse(input, output);
	}

	public void parse(Path input, Path output) throws Exception {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		Handler handler = new Handler();
		writer = Files.newBufferedWriter(output);
		// parser.parse(input.toFile(), handler);
		Files.list(input).forEach(p -> {
			try {
				parser.parse(p.toFile(), handler);
				writer.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	private class Handler extends DefaultHandler {

		private HandlerState state;
		private StringBuilder buffer;
		private List<String> topics;

		@Override
		public void startDocument() throws SAXException {
			enter(HandlerState.IN_BETWEEN);
			buffer = new StringBuilder();
			topics = new ArrayList<>();
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			switch (qName) {
			case "headline":
				enter(HandlerState.HEADLINE);
			case "text":
				enter(HandlerState.TEXT);
			case "codes":
				if (attributes.getValue("class") != null
						&& attributes.getValue("class")
								.equals("bip:topics:1.0"))
					enter(HandlerState.TOPICS);
			case "code":
				if (currentlyIn(HandlerState.TOPICS)
						&& attributes.getValue("code") != null)
					topics.add(attributes.getValue("code"));
			default:
				break;
			}
		}

		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (currentlyIn(HandlerState.HEADLINE)
					|| currentlyIn(HandlerState.TEXT))
				buffer.append(new String(ch, start, length));
		}

		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ((!currentlyIn(HandlerState.IN_BETWEEN))
					&& (qName.equals("headline") || qName.equals("text") || qName
							.equals("codes")))
				enter(HandlerState.IN_BETWEEN);
		}

		@Override
		public void endDocument() throws SAXException {
			process(buffer.toString(), topics);
		}

		private void enter(HandlerState state) {
			this.state = state;
		}

		private boolean currentlyIn(HandlerState state) {
			return state.equals(this.state);
		}
	}

	private enum HandlerState {
		IN_BETWEEN, TEXT, TOPICS, HEADLINE
	}

	private void process(String text, List<String> topics) {
		count++;
		if (count % 8068 == 0)
			System.out.println(count * 100 / 806800 + "% done");
		if (topics.size() == 1)
			try {
				writer.write(topics.get(0) + " "
						+ SentenceProcessing.toProcessedString(text) + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
