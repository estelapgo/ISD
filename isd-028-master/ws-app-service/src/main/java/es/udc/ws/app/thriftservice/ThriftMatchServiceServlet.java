package es.udc.ws.app.thriftservice;

import es.udc.ws.util.servlet.ThriftHttpServletTemplate;
import es.udc.ws.app.thrift.*;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;

public class ThriftMatchServiceServlet extends ThriftHttpServletTemplate {

    public ThriftMatchServiceServlet() {
        super(createProcessor(),crearteProtocolFactory());
    }

    public static TProcessor createProcessor() {
        return new ThriftMatchService.Processor<ThriftMatchService.Iface>(
                new ThriftMatchServiceImpl());
    }

    public static TProtocolFactory crearteProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }
}
