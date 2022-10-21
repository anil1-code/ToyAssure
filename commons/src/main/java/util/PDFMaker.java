package util;

import exception.ApiException;
import model.data.InvoiceData;
import org.apache.fop.apps.*;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class PDFMaker {

    public static byte[] makePdf(InvoiceData invoiceData) throws ApiException {
        String xml = jaxbObjectToXML(invoiceData);
        String xsltPath = "../commons/src/main/resources/invoice.xml";
        try {
            return convertToPDF(xsltPath, xml);
        } catch (IOException e) {
            throw new ApiException("IO Exception while generating invoice");
        }
    }

    private static String jaxbObjectToXML(InvoiceData invoiceData) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceData.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter stringWriter = new StringWriter();
            jaxbMarshaller.marshal(invoiceData, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static byte[] convertToPDF(String xsltPath, String xml) throws ApiException, IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            File f = new File(xsltPath);
            StreamSource streamSource = new StreamSource(f);
            Transformer transformer = factory.newTransformer(streamSource);
            Source src = new StreamSource(new StringReader(xml));
            Result res = new SAXResult(fop.getDefaultHandler());
            transformer.transform(src, res);
        } catch (FOPException e) {
            throw new ApiException(e.getMessage());
        } catch (TransformerException e) {
            throw new ApiException(e.getMessage());
        } finally {
            out.close();
        }
        return out.toByteArray();
    }
}
