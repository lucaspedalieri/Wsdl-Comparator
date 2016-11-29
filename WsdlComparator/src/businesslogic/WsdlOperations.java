package businesslogic;

import java.io.InputStream;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import org.apache.log4j.BasicConfigurator;

import com.predic8.soamodel.Difference;
import com.predic8.wsdl.Definitions;
import com.predic8.wsdl.WSDLParser;
import com.predic8.wsdl.diff.WsdlDiffGenerator;

public class WsdlOperations {

	private Thread mProcess;
	private List<Difference> resultCompare;

	public WsdlOperations() {
	}

	public void stopThread() {
		if (mProcess != null) {
			mProcess.stop();
			mProcess.interrupt();
		}
	}

	private void notifyFromThread() {
		synchronized (this) {
			notify();
		}
	}

	public List<Difference> getResultCompare() {
		return resultCompare;
	}

	public void setResultCompare(List<Difference> resultCompare) {
		this.resultCompare = resultCompare;
	}

	public List<Difference> compareWsdl(final String pathWsdl1, final String pathWsdl2) {
		mProcess = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// configure the log4j logger
					BasicConfigurator.configure();

					// create a new wsdl parser
					WSDLParser parser = new WSDLParser();

					// parse both wsdl documents
					Definitions wsdl1 = parser.parse(pathWsdl1);
					Definitions wsdl2 = parser.parse(pathWsdl2);

					// compare the wsdl documents
					WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);
					setResultCompare(diffGen.compare());

				} catch (Exception e) {
					// error
					System.out.println(e.getMessage());
				} finally {
					notifyFromThread();
				}
			}
		});
		mProcess.start();

		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		return getResultCompare();

	}

	public List<Difference> compareWsdl(final InputStream pathWsdl1, final InputStream pathWsdl2) {
		mProcess = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					// configure the log4j logger
					BasicConfigurator.configure();

					// create a new wsdl parser
					WSDLParser parser = new WSDLParser();

					// parse both wsdl documents
					Definitions wsdl1 = parser.parse(pathWsdl1);
					Definitions wsdl2 = parser.parse(pathWsdl2);

					// compare the wsdl documents
					WsdlDiffGenerator diffGen = new WsdlDiffGenerator(wsdl1, wsdl2);
					setResultCompare(diffGen.compare());

				} catch (Exception e) {
					// error
					System.out.println(e.getMessage());
				} finally {
					notifyFromThread();
				}
			}
		});
		mProcess.start();

		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}

		return getResultCompare();
	}
}
