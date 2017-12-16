package wg_gesucht;

import java.io.IOException;
import java.util.LinkedList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class MessageWriter {

	public static final String filePathMessages = "./rsc/messages";
	
	public MessageWriter(DocSplit ds) throws InterruptedException, IOException {
		writeMsgs(ds.d1);
		System.out.println();
		writeMsgs(ds.d2);
	}
	
	public void writeMsgs(Document[] docs) throws InterruptedException, IOException
	{
		for (Document doc : docs)
		{
			char contactGender;
			String bgImgStyle = doc.selectFirst("div[class=\"profile_image_dav\"]").attr("style");
			if (bgImgStyle.contains("/female.png"))
			{
				contactGender = 'f';
			}
			else if (bgImgStyle.contains("/male.png"))
			{
				contactGender = 'm';
			}
			else
			{
				contactGender = 'n';
			}
			
			String url = doc.selectFirst("a[class=\"btn btn-block btn-md btn-orange\"]").attr("href");
			Document contactForm = URLconnector.connect(url);
			Element contactNameContainer = contactForm.getElementsContainingOwnText("Nachricht an").first();
			String[] contactNameText = contactNameContainer.text().split(" ");
			
			StringBuilder sb = new StringBuilder();
			LinkedList<String> nameComponents = new LinkedList<String>();
			int i = 2;
			while (!contactNameText[i].equals("senden:"))
			{
				nameComponents.add(contactNameText[i]);
				sb.append(contactNameText[i]);
				sb.append(" ");
				i++;
			}
			sb.deleteCharAt(sb.length()-1);
			String contactName = sb.toString();
			
			String salutation;
			System.out.print(contactName+": ");
			switch(contactGender)
			{
			case 'f':
				//informal
				if (nameComponents.size() == 1) salutation = "Liebe "+nameComponents.get(0)+", ";
				//formal
				else salutation = "Sehr geehrte Frau "+nameComponents.get(nameComponents.size() - 1)+", ";
				break;
			case 'm':
				//informal
				if (nameComponents.size() == 1) salutation = "Lieber "+nameComponents.get(0)+", ";
				//formal
				else salutation = "Sehr geehrter Herr "+nameComponents.get(nameComponents.size() - 1)+", ";
				break;
			default:
				salutation = "Hallo "+contactName+", ";
				break;
			}
			System.out.print(salutation);
			System.out.println();
		}
	}
}